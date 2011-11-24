package com.linkedin.frameworks

import org.scalatest._
import org.scalatest.matchers._

class DustSpec extends Spec with ShouldMatchers
{
  val dust = new Dust()

  describe("First dust example")
  {
    it ("should make a simple script")
    {
      val result = dust.example
      result should be ("3.0")
    }

    it("should render the first example correctly")
    {
      val template = classOf[DustSpec].getResourceAsStream("hello.dust")
      val data = classOf[DustSpec].getResourceAsStream("hello.data.json")
      time ("compile") {
        dust.compile(template)
      }
      var result = ""
      time("render") {
        result = dust.render(data)
      }
      result should be ("Hello Mick! You have 30 new messages.")
    }
  }
  
  def time(title : String = "script")(f : => Unit)
  {
    val then = System.currentTimeMillis
    f
    println("%s took %d milliseconds".format(title, System.currentTimeMillis - then))
  }
}