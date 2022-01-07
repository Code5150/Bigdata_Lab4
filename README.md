# Лабораторная работа № 4: Zookeeper
***

## Цель работы:

* запустить ZooKeeper,
* изучить директорию с установкой ZooKeeper,
* запустить интерактивную сессию ZooKeeper CLI и освоить её команды,
* научиться проводить мониторинг ZooKeeper,
* разработать приложение с барьерной синхронизацией, основанной на ZooKeeper,
* запустить и проверить работу приложения.


## Выполнение заданий:

Была изучена интерактивная сессия Zookeeper и было изучена работа приложения с барьерной синхронизацией.

Результат работы:

![Приложение с барьерной синхронизацией](https://github.com/Code5150/Bigdata_Lab4/blob/master/img/animal.jpg)

## Упражнения:

### Задача об обедающих философах.

__Постановка задачи__:

*"Несколько безмолвных философов сидят вокруг круглого стола, перед каждым философом стоит тарелка спагетти. Вилки лежат на столе между каждой парой ближайших философов.
Каждый философ может либо есть, либо размышлять. Приём пищи не ограничен количеством оставшихся спагетти — подразумевается бесконечный запас. Тем не менее, философ может есть только тогда, когда держит две вилки — взятую справа и слева (альтернативная формулировка проблемы подразумевает миски с рисом и палочки для еды вместо тарелок со спагетти и вилок).
Каждый философ может взять ближайшую вилку (если она доступна) или положить — если он уже держит её. Взятие каждой вилки и возвращение её на стол являются раздельными действиями, которые должны выполняться одно за другим.
Вопрос задачи заключается в том, чтобы разработать модель поведения (параллельный алгоритм), при котором ни один из философов не будет голодать, то есть будет вечно чередовать приём пищи и размышления."*

__Решение__:

При решении задачи о философах Zookeeper использовался в качестве официанта, разрешающего или запрещающего философам садиться за стол.

```
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
```

![Начало обеда](https://github.com/Code5150/Bigdata_Lab4/blob/master/img/philosophersStart.jpg)

![Окончание обеда](https://github.com/Code5150/Bigdata_Lab4/blob/master/img/philosophersEnd.jpg)

## Заключение

В ходе выполнения лабораторной работы были изучены основные возможности Zookeeper. Взаимодействие с Zookeeper осуществлялось через CLI, а при разработке приложений -  через Zookeeper Java API.