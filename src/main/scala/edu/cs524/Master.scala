package edu.cs524

trait Master extends NetworkNode {
  var currentJob: Job = null
  var isJobRunning: Boolean = false

  def SubmitJob(job: Job) = {
    if (currentJob == null) {
      currentJob = job
      currentJob.Start()
    }
  }

  def CompleteTask(taskId: String) = {
    this.synchronized {
      if (currentJob.CompleteTask(taskId)) {
        currentJob = null
        isJobRunning = false
      }
    }
  }
}
