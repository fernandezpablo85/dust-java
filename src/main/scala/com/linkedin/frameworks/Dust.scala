package com.linkedin.frameworks

import org.mozilla.javascript._
import scala.io._
import java.io._

class Dust
{
  val context = Context.enter
  val scope = context.initStandardObjects
  loadDust(scope)
  
  def compile (template : InputStream)
  {
    val templateString = getString(template)
    loadTemplate(templateString)
  }

  def render (data : InputStream, template : String = "template") = 
  {
    val dataString = getString(data)
    val rendered = renderTemplate(dataString, template)
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
    val dust = classOf[Dust].getResourceAsStream("dust.js")
    context.evaluateString(scope, getString(dust), "<dust-loading>", 1, null)
  }

  private def loadTemplate(template : String, name : String = "template")
  {
    val script = """
      var compiled = dust.compile("%s", "%s");
      dust.loadSource(compiled);
    """.format(template, name)
    context.evaluateString(scope, script, "<template-loading>", 2, null);
  }

  private def renderTemplate(data : String, name : String) = 
  {
    val script2 = """
       var result = 'not finished';
       dust.render("%s", %s, function(err, out) {
         result = out;
       });
       result;
     """.format(name, data)
     context.evaluateString(scope, script2, "<template-rendering>", 2, null)
  }
}

object Dust
{

}
