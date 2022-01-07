package com.vladislav

import java.util.concurrent.Semaphore
import scala.util.Random

object Lab4 {
  def main(args: Array[String]): Unit = {
    //zoo()
    philosophers()
  }

  def philosophers(): Unit = {
    println("Dining philosophers")
    val hostPort = "localhost:2181"
    val philosophersCount = 4
    val seats = philosophersCount - 1

    val forks = new Array[Semaphore](philosophersCount)
    for (j <- 0 until philosophersCount){
      forks(j) = new Semaphore(1)
    }

    val threads = new Array[Thread](philosophersCount)
    for (id <- 0 until philosophersCount){
      threads(id) = new Thread(
        () => {
          val i = (id + 1) % philosophersCount
          val philosopher = Philosopher(id, hostPort, "/phil", forks(id), forks(i), seats)
          for (_ <- 1 to 3) {
            philosopher.eat()
            philosopher.think()
          }
        }
      )
      threads(id).setDaemon(false)
      threads(id).start()
    }
    for (id <- 0 until philosophersCount){
      threads(id).join()
    }
  }

  def zoo(): Unit = {
    println("Starting animal runner")
    val animalName = "Bear"
    val hostPort = "localhost:2181"
    val partySize = 1
    val animal = Animal(animalName, hostPort, "/zoo", partySize)

    try {
      animal.enter()
      println(s"${animal.name} entered.")
      for (i <- 1 to Random.nextInt(10)) {
        Thread.sleep(3000)
        println(s"${animal.name} is running...")
      }
      animal.leave()

    } catch {
      case e: Exception => println("Animal was not permitted to the zoo." + e)
    }
  }
}
