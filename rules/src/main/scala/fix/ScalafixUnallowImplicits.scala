package fix

import scalafix.v1._
import scala.meta._

class ScalafixUnallowImplicits extends SyntacticRule("Scalafixmyrules") {

  override def isLinter: Boolean = true

  val disallowImplicit = "scala.concurrent.ExecutionContext.global"

  override def fix(implicit doc: SyntacticDocument): Patch = {
    val disallowImplicitParent =
      disallowImplicit.split("\\.").dropRight(1).mkString(".")
    val importeeName = disallowImplicit.split("\\.").last

    doc.tree.collect { case i: Import =>
      i.importers.collect {
        case importer 
            if importer.ref.syntax == disallowImplicitParent &&
              (importer.importees.exists {
                case _: Importee.Wildcard => true
                case nm: Importee.Name    => nm.name.toString == importeeName
                case nm: Importee.Rename  => nm.name.toString == importeeName
              }) =>

        Patch.lint(Diagnostic.apply("illegal.import", "Illegal import", importer.pos))
      }
    }.flatten.asPatch

  }

}
