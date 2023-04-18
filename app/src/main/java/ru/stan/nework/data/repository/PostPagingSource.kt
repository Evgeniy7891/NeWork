package ru.stan.nework.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.providers.network.NetworkService
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostPagingSource(private val apiService: NetworkService) : PagingSource<Long, Post>() {
    override fun getRefreshKey(state: PagingState<Long, Post>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        try {
            val result = when (params) {
                is LoadParams.Refresh -> {
                    apiService.getLatest(params.loadSize)
                }
                is LoadParams.Append -> {
                    apiService.getBefore(params.key, params.loadSize)
                }
                is LoadParams.Prepend -> return LoadResult.Page(
                    data = emptyList(), nextKey = null, prevKey = params.key
                )
            }
            if (!result.isSuccessful) {
                throw HttpException(result)
            }
            val data = result.body() ?: throw RuntimeException("${result.code()}")

            val nextKey = if (data.isEmpty()) null else data.last().id?.toLong()
            return LoadResult.Page(data = data, prevKey = params.key, nextKey = nextKey)

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
