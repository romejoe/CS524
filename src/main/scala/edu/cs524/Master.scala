package edu.cs524

trait Master extends NetworkNode{
  def SubmitJob(job:Job)
  def CompleteTask(task:Task)
}
