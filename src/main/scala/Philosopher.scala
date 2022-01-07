package com.vladislav

import java.util.concurrent.Semaphore
import org.apache.zookeeper._
import scala.util.Random

case class Philosopher(id: Int, hostPort: String, root: String,
                       left: Semaphore, right: Semaphore, seats: Int) extends Watcher {

  val zk = new ZooKeeper(hostPort, 3000, this)
  val mutex = new Object()
  val path: String = root + "/" + id.toString

  if (zk == null) throw new Exception("ZK is NULL.")

  override def process(event: WatchedEvent): Unit = {
    mutex.synchronized {
      mutex.notify()
    }
  }

  def eat(): Boolean = {
    println("Philosopher " + id + " starts eating")
    mutex.synchronized {
      var created = false
      while (true) {
        if (!created) {
          zk.create(path, Array.emptyByteArray, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL)
          created = true
        }
        val active = zk.getChildren(root, this)
        if (active.size() > seats) {
          zk.delete(path, -1)
          mutex.wait(3000)
          Thread.sleep(Random.nextInt(5000) + 1000)
          created = false
        } else {
          left.acquire()
          println("Philosopher " + id + " took left fork")
          right.acquire()
          println("Philosopher " + id + " took right fork")
          Thread.sleep(Random.nextInt(5000) + 1000)
          left.release()
          println("Philosopher " + id + " released left fork")
          right.release()
          println("Philosopher " + id + " released right fork")
          println("Philosopher " + id + " finished eating")
          return true
        }
      }
    }
    false
  }

  def think(): Unit = {
    println("Philosopher " + id + " is thinking")
    zk.delete(path, -1)
    Thread.sleep(Random.nextInt(5000) + 1000)
  }
}
