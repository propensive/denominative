/*
    Denominative, version [unreleased]. Copyright 2024 Jon Pretty, Propensive OÜ.

    The primary distribution site is: https://propensive.com/

    Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
    file except in compliance with the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software distributed under the
    License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
    either express or implied. See the License for the specific language governing permissions
    and limitations under the License.
*/

package denominative

import rudiments.*
import anticipation.*

final val Prim = Ordinal.fromOne(1)
final val Sec  = Ordinal.fromOne(2)
final val Ter  = Ordinal.fromOne(3)
final val Quat = Ordinal.fromOne(4)
final val Quin = Ordinal.fromOne(5)
final val Sen  = Ordinal.fromOne(6)
final val Sept = Ordinal.fromOne(7)
final val Oct  = Ordinal.fromOne(8)
final val Non  = Ordinal.fromOne(9)
final val Den  = Ordinal.fromOne(10)

extension (inline cardinal: Int)
  @targetName("plus")
  inline infix def + (inline ordinal: Ordinal): Ordinal = Ordinal.fromZero(cardinal + ordinal.fromZero)

object Denominative:
  opaque type Ordinal = Int
  opaque type Interval = Long

  // FIXME: This is implemented as a non-inlined method because it gets shadowed by the other minus method (with
  // the same name) when it's inline.
  extension (ordinal: Ordinal)
    @targetName("minus2")
    infix def - (right: Ordinal): Int = ordinal - right

  extension (inline ordinal: Ordinal)
    @targetName("plus")
    inline infix def + (inline cardinal: Int): Ordinal = ordinal + cardinal

    @targetName("minus")
    inline infix def - (inline cardinal: Int): Ordinal = ordinal - cardinal
    
    @targetName("lessThanOrEqualTo")
    inline infix def <= (inline right: Ordinal): Boolean = ordinal <= right
    
    @targetName("greaterThanOrEqualTo")
    inline infix def >= (inline right: Ordinal): Boolean = ordinal >= right

    inline def next: Ordinal = ordinal + 1
    inline def previous: Ordinal = ordinal - 1

    @targetName("to")
    inline infix def ~ (inline right: Ordinal): Interval = Interval(ordinal, right)

    inline def fromZero: Int = ordinal - 1
    inline def fromOne: Int = ordinal

    inline def degenerate: Boolean = ordinal < 1

  object Ordinal:
    inline def fromZero(inline cardinal: Int): Ordinal = cardinal + 1
    inline def fromOne(inline cardinal: Int): Ordinal = cardinal

    given show: Textualizer[Ordinal] =
      case Prim    => "prim".tt
      case Sec     => "sec".tt
      case Ter     => "ter".tt
      case Quat    => "quat".tt
      case Quin    => "quin".tt
      case Sen     => "sen".tt
      case Sept    => "sept".tt
      case Oct     => "oct".tt
      case Non     => "non".tt
      case Den     => "den".tt
      case ordinal => if ordinal.degenerate then "degenerate".tt else ("Ordinal.fromOne("+ordinal+")").tt

  extension (interval: Interval)
    inline def start: Ordinal = ((interval >> 32) & 0xffffffff).toInt
    inline def end: Ordinal = (interval & 0xffffffff).toInt
    inline def contains(ordinal: Ordinal): Boolean = start <= ordinal && ordinal <= end
    inline def size: Int = (end - start) max 0

    inline def each(inline lambda: Ordinal => Unit): Unit =
      var i: Ordinal = start
      
      while i <= end do
        lambda(i)
        i = i.next

    inline def foldLeft[ValueType](inline initial: ValueType)(inline lambda: (ValueType, Ordinal) => ValueType)
            : ValueType =

      var i: Ordinal = start
      var acc: ValueType = initial
      
      while i <= end do
        acc = lambda(acc, i)
        i = i.next
      acc

    inline def empty: Boolean = end < start
  
  object Interval:
    inline def apply(inline start: Ordinal, inline end: Ordinal): Interval =
      (start & 0xffffffffL) << 32 | end & 0xffffffffL

object Countable:
  given sequence[ElementType]: Countable[Seq[ElementType]] = new Countable[Seq[ElementType]]:
    inline def zeroIndexed: true = true
    inline def ult(inline sequence: Seq[ElementType]): Ordinal = Ordinal.fromOne(sequence.length)

trait Countable[-SequenceType]:
  inline def zeroIndexed: Boolean
  inline def ult(inline sequence: SequenceType): Ordinal
  
  inline def index(inline ordinal: Ordinal): Int =
    inline if zeroIndexed then ordinal.fromZero else ordinal.fromOne

extension [CountableType](inline value: CountableType)(using countable: Countable[CountableType])
  inline def ult: Ordinal = countable.ult(value)
  inline def pen: Ordinal = countable.ult(value).previous
  inline def ante: Ordinal = countable.ult(value).previous.previous

export Denominative.{Ordinal, Interval}