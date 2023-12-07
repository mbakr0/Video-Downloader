package online.mohmedbakr.downloadvideos.work

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import online.mohmedbakr.downloadvideos.database.VideoDataBase
import online.mohmedbakr.downloadvideos.repository.LocalVideoRepository

class UpdateDatabaseWorker(appContext: Context, workerParams: WorkerParameters) : RxWorker(appContext, workerParams) {
    override fun createWork(): Single<Result> {
        val dataBase = VideoDataBase.getInstance(applicationContext)
        return Single.create {emitter->
            runCatching {
                LocalVideoRepository(dataBase).refreshDatabase()
            }.onFailure {
                emitter.onError(it)
            }.onSuccess {
                emitter.onSuccess(Result.success())
            }
        }
            .onErrorReturn{ Result.failure() }
            .map { Result.success() }
    }

    override fun getBackgroundScheduler(): Scheduler {
        return Schedulers.io()
    }

    companion object {
        const val WORK_NAME = "UpdateDatabaseWorker"
    }
}