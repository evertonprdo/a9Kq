package dev.evertonprdo.a9kq.features.meterreading.add

data class AddMeterReadingUiState(
    val meterIndex: Int? = null,
    val submissionState: Submission = Submission.Idle,
) {
    val canBeSubmitted: Boolean
        get() = meterIndex != null && submissionState is Submission.Idle

    sealed class Submission {
        data object Idle : Submission()
        data object Submitting : Submission()
        data class Failure(val cause: Throwable) : Submission()
    }

    companion object {
        fun toIdle() = Submission.Idle
        fun toSubmitting() = Submission.Submitting
        fun toFailure(cause: Throwable) = Submission.Failure(cause)
    }
}
