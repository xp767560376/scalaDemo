package test.service

object Demo3 extends App {
  var mapCount = Map[String,Int]()
  mapCount+=("aa"->10)
  println(mapCount.get("aa"))
}
