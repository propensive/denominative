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

import annotation.targetName

final val Prim: Ordinal = Ordinal.natural(1)
final val Sec: Ordinal  = Ordinal.natural(2)
final val Ter: Ordinal  = Ordinal.natural(3)
final val Quat: Ordinal = Ordinal.natural(4)
final val Quin: Ordinal = Ordinal.natural(5)
final val Sen: Ordinal  = Ordinal.natural(6)
final val Sept: Ordinal = Ordinal.natural(7)
final val Oct: Ordinal  = Ordinal.natural(8)
final val Non: Ordinal  = Ordinal.natural(9)
final val Den: Ordinal  = Ordinal.natural(10)

inline def Ult: Countback = Countback(0)
inline def Pen: Countback = Countback(1)
inline def Ant: Countback = Countback(2)
inline def Pre: Countback = Countback(3)

extension (inline cardinal: Int)
  @targetName("plus")
  inline infix def + (inline ordinal: Ordinal): Ordinal =
    Ordinal.zerary(cardinal + ordinal.n0)

extension [ValueType: Countable](inline value: ValueType)
  inline def ult: Ordinal = ValueType.ult(value)
  inline def pen: Ordinal = ValueType.ult(value).previous
  inline def ant: Ordinal = ValueType.ult(value).previous.previous
  inline def full: Interval = Interval(Prim, ult)

export Denominative.{Ordinal, Interval}
export Denominative2.{Countback, Confinement}