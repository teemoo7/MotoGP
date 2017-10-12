package main.scala.ch.afidis.motogp.parser

import State._
import util.parsing.combinator.RegexParsers
import util.matching.Regex
import com.sun.corba.se.impl.activation.StartServer

/**
 * Created with IntelliJ IDEA.
 * User: Micael
 * Date: 25.10.12
 * Time: 08:55
 * To change this template use File | Settings | File Templates.
 */
object Parser extends RegexParsers  {
  private def digit = """[0-9]""".r
  private def number = """[1-9][0-9]*""".r
  private def year = """(19|20)[0-9][0-9]""".r
  private def anyNonEmpty = """.+""".r
  private def position = """([1-9][0-9]*\s?\.)?""".r
  private def riderName = """[a-zA-Z]+\.?\s?[a-zA-Z]+""".r
  private def nationalityCode = """[A-Z]+""".r
  private def constructorName = """[a-zA-Z]+([\s\-][a-zA-Z]+)*""".r

  private def pageBreak = """= Page""".r ~ number ~ """=""".r ^^ {
    case p ~ number ~ e => number.toInt
  }

  private def sectionStart = """Section""".r ~ """[1-9]""".r ~ """:""".r ^^ {
    case s ~ number ~ e => number.toInt
  }
  private def sectionYearRange = year ~ """-""".r ~ year ^^ {
    case yearStart ~ t ~ yearEnd => (yearStart, yearEnd)
  }

  private def championshipRiderTitle = year ~ """Riders\S""".r ~ anyNonEmpty ^^ {
    case year ~ r ~ title => (year, title)
  }
  private def championshipRiderStanding = position ~ riderName ~ """\(""".r ~ nationalityCode ~ """\)""".r ~ constructorName ~ number ^^ {
    case position ~ rider ~ p1 ~ nationality ~ p2 ~ constructor ~ points => (position, rider, nationality, constructor, points)
  }
  private def championshipConstructorTitle = year ~ """Constructors\S""".r ~ anyNonEmpty ^^ {
    case year ~ c ~ title => (year, title)
  }
  private def championshipConstructorStanding = position ~ constructorName ~ number ^^ {
    case position ~ constructor ~ points => (position, constructor, points)
  }

  def parse(str: String, expectedState: State): ParseResult[Any] = {
    expectedState match {
      case SectionStart => parse(sectionStart, str)
      case SectionYearRange => parse(sectionYearRange, str)
      case SectionCategory => parse(anyNonEmpty, str)
      case SectionChampionship => parse(anyNonEmpty, str)

      case ChampionshipYear => parse(year, str)
      case ChampionshipRiderTitle => parse(championshipRiderTitle, str)
      case ChampionshipRiderStanding => parse(championshipRiderStanding, str)
      case ChampionshipConstructorTitle  => parse(championshipConstructorTitle, str)
      case ChampionshipConstructorStanding => parse(championshipConstructorStanding, str)
    }
  }

  def parsePageBreak(str: String): ParseResult[Int] = {
    parse(pageBreak, str)
  }
}
