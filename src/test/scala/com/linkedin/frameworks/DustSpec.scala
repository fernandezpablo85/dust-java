package com.linkedin.frameworks

import org.scalatest._
import org.scalatest.matchers._

class DustSpec extends Spec with ShouldMatchers
{
  describe("First dust example")
  {
    it ("should make a simple script")
    {
      val result = Dust.example
      result should be ("3.0")
    }

    it("should render the first example correctly")
    {
      val template = classOf[DustSpec].getResourceAsStream("hello.dust")
      val data = classOf[DustSpec].getResourceAsStream("hello.data.json")

      val result = Dust.render(template, data)

      result should be ("Hello Mick! You have 30 new messages.")
    }
  }
}