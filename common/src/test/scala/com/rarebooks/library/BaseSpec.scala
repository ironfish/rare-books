package com.rarebooks.library

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{ Inspectors, Matchers, WordSpec }

abstract class BaseSpec extends WordSpec with Matchers with TypeCheckedTripleEquals with Inspectors
