package main.scala.ch.afidis.motogp.parser

import io.Source
import State._

/**
 * Created with IntelliJ IDEA.
 * User: Micael
 * Date: 30.10.12
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
object reader {
  var currentState: State = Start
  var currentPage: Int = 0

  val transitions = Map(
    Start -> Array(SectionStart),
    SectionStart -> Array(SectionYearRange),
    SectionYearRange -> Array(SectionCategory),
    SectionCategory -> Array(SectionChampionship),
    SectionChampionship -> Array(ChampionshipYear),
    ChampionshipYear -> Array(ChampionshipRiderTitle),
    ChampionshipRiderTitle -> Array(ChampionshipRiderStanding),
    ChampionshipRiderStanding -> Array(ChampionshipRiderStanding, ChampionshipConstructorTitle),
    ChampionshipConstructorTitle -> Array(ChampionshipConstructorStanding),
    ChampionshipConstructorStanding -> Array(ChampionshipConstructorStanding, End)
  )

  def main(args: Array[String]) {

    Source.fromFile("b.txt").getLines.foreach{
      case rawLine => {
        // Line read
        val line = cleanLine(rawLine)
        if (line.length > 0) {
          transitions.get(currentState) match {
            case Some(possibleStates) => {
              // Page break
              Parser.parsePageBreak(line) match {
                case Parser.Success(result, _) => {
                  updateCurrentPage(result)
                }
                case _ => {
                  // States
                  var matching = false
                  for (state <- possibleStates) {
                    if (!matching) {
                      Parser.parse(line, state) match {
                        case Parser.Success(result, _) => {
                          updateCurrentState(state)
                          ok(result.toString)
                          matching = true
                        }
                        case _ =>
                      }
                    }
                  }
                  if (!matching) {
                    error(line)
                  }
                }
              }
            }
            case _ => {
              error("[ERROR] State")
              sys.exit()
            }
          }
        }
      }
    }
  }

  def cleanLine(rawLine: String): String = {
    rawLine.trim
  }

  def error(line: String) = {
    println("[ERROR] no matching found for line: ["+line+"], previous state ["+currentState+"]")
    sys.exit()
  }

  def updateCurrentState(newState: State) {
    currentState = newState
  }

  def updateCurrentPage(newPage: Int) {
    currentPage = newPage
    println("[INFO] End of page "+currentPage)
  }

  def ok(str: String) {
    println("[OK] "+currentState + " : "+str)
  }

}
