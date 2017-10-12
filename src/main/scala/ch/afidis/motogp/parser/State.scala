package main.scala.ch.afidis.motogp.parser

/**
 * Created with IntelliJ IDEA.
 * User: Micael
 * Date: 02.11.12
 * Time: 08:51
 * To change this template use File | Settings | File Templates.
 */
object State extends Enumeration {
  type State = Value
  val
  Start,
  End,

  SectionStart,
  SectionYearRange,
  SectionCategory,
  SectionChampionship,

  ChampionshipYear,
  ChampionshipRiderTitle,
  ChampionshipRiderStanding,
  ChampionshipConstructorTitle,
  ChampionshipConstructorStanding

  = Value
}
