package com.linkedin.frameworks

import org.mozilla.javascript._
import scala.io._
import java.io._

class Dust
{
  val context = Context.enter
  def render (template : InputStream, data : InputStream) =
  {
    val templateString = getString(template)
    val dataString = getString(data)
    val scope = context.initStandardObjects()

    context.evaluateString(scope, "var window = {}; window.dust = {};", "<dust-loading-0>", 1, null)

    val dust = classOf[Dust].getResourceAsStream("dust.js")
    context.evaluateString(scope, getString(dust), "<dust-loading>", 1, null)
    val script = """
      var compiled = window.dust.compile("%s", "%s");
      dust.loadSource(compiled);
    """.format(templateString, "name")
    context.evaluateString(scope, script, "<template-loading>", 2, null);

     val script2 = """
        var result = 'not finished';
        dust.render("%s", %s, function(err, out) {
          result = out;
        });
        result;
      """.format("name", dataString)
      val rendered = context.evaluateString(scope, script2, "<template-rendering>", 2, null)
      
    rendered.toString
  }
  
  def example =
  {
    val scope = context.initStandardObjects
    context.evaluateString(scope, "var a = 1", "definition", 1, null)
    context.evaluateString(scope, "a = a + 2", "adition", 2, null)
    context.evaluateString(scope, "a", "result", 3, null).toString
  }

  private def getString (is :InputStream) = Source.fromInputStream(is).getLines.mkString("\n")

  private def loadDust (scope : Scriptable) =
  {

  }

  private def loadTemplate(scope : Scriptable, template : String, name : String = "template")
  {

  }

  private def renderTemplate(scope : Scriptable, data : String, name : String = "template") = 
  {
   
  }
}

object Dust
{
  def render (template : InputStream, data : InputStream) = new Dust().render(template, data)
  def example = new Dust().example
}
