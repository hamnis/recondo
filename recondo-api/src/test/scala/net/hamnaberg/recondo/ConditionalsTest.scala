package net.hamnaberg.recondo

import org.joda.time.DateTime
import org.junit.{Assert, Test}

class ConditionalsTest {

  @Test
  def testCreateEmpty() {
    val conditionals = Conditionals()
    Assert.assertTrue(conditionals.ifMatch.isEmpty)
    Assert.assertTrue(conditionals.ifNonMatch.isEmpty)
    Assert.assertTrue(conditionals.ifModifiedSince == None)
    Assert.assertTrue(conditionals.ifUnModifiedSince == None)
  }

  @Test
  def testAddNoneMatch() {
    val conditionals = Conditionals().addIfNoneMatch(new Tag("124", false))
    Assert.assertTrue("Match was not empty", conditionals.ifMatch.isEmpty)
    Assert.assertFalse("None match was empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == None)
    Assert.assertTrue("un-modified since was not 'None'", conditionals.ifUnModifiedSince == None)
  }

  @Test
  def testMultipleAddNoneMatch() {
    val conditionals = Conditionals().addIfNoneMatch(new Tag("124", false)).addIfNoneMatch(new Tag("123", false))
    Assert.assertTrue("Match was not empty", conditionals.ifMatch.isEmpty)
    Assert.assertFalse("None match was empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertEquals("None match size was wrong", 2, conditionals.ifNonMatch.length)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == None)
    Assert.assertTrue("un-modified since was not 'None'", conditionals.ifUnModifiedSince == None)
  }

  @Test
  def testAddMatch() {
    val conditionals = Conditionals().addIfMatch(new Tag("124", false))
    Assert.assertFalse("Match was empty", conditionals.ifMatch.isEmpty)
    Assert.assertEquals("Match had wrong size", 1, conditionals.ifMatch.length)
    Assert.assertTrue("None match not was empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == None)
    Assert.assertTrue("un-modified since was not 'None'", conditionals.ifUnModifiedSince == None)
  }  

  @Test
  def testMultipleAddMatch() {
    val conditionals = Conditionals().addIfMatch(new Tag("124", false)).addIfMatch(new Tag("123", false))
    Assert.assertFalse("Match was not empty", conditionals.ifMatch.isEmpty)
    Assert.assertTrue("None match was empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertEquals("None match size was wrong", 2, conditionals.ifMatch.length)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == None)
    Assert.assertTrue("un-modified since was not 'None'", conditionals.ifUnModifiedSince == None)
  }

  @Test
  def testAddMatchAndNoneMatch() {
    val tag = new Tag("124", false)
    val conditionals = Conditionals().addIfMatch(tag).addIfNoneMatch(tag)
    Assert.assertTrue("Match was not empty", conditionals.ifMatch.isEmpty)
    Assert.assertFalse("None match was empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertEquals("NoneMatch had wrong size", 1, conditionals.ifNonMatch.length)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == None)
    Assert.assertTrue("un-modified since was not 'None'", conditionals.ifUnModifiedSince == None)
  }

  @Test
  def testAddMatchDuplicate() {
    val tag = new Tag("124", false)
    val conditionals = Conditionals().addIfMatch(tag).addIfMatch(tag)
    Assert.assertFalse("Match was not empty", conditionals.ifMatch.isEmpty)
    Assert.assertEquals("NoneMatch had wrong size", 1, conditionals.ifMatch.length)
    Assert.assertTrue("None match was empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == None)
    Assert.assertTrue("un-modified since was not 'None'", conditionals.ifUnModifiedSince == None)
  }

  @Test
  def testAddNoneMatchDuplicate() {
    val tag = new Tag("124", false)
    val conditionals = Conditionals().addIfNoneMatch(tag).addIfNoneMatch(tag)
    Assert.assertTrue("Match was not empty", conditionals.ifMatch.isEmpty)
    Assert.assertFalse("None match was not empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertEquals("NoneMatch had wrong size", 1, conditionals.ifNonMatch.length)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == None)
    Assert.assertTrue("un-modified since was not 'None'", conditionals.ifUnModifiedSince == None)
  }

  @Test
  def testAddMatchThenAddALL() {
    val tag = new Tag("124", false)
    val conditionals = Conditionals().addIfMatch(tag).addIfMatch(Tag.ALL)
    Assert.assertFalse("Match was empty", conditionals.ifMatch.isEmpty)
    Assert.assertEquals("Match had wrong size", 1, conditionals.ifMatch.length)
    Assert.assertEquals("Match was not equal", List(Tag.ALL), conditionals.ifMatch)
    Assert.assertTrue("None match was not empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == None)
    Assert.assertTrue("un-modified since was not 'None'", conditionals.ifUnModifiedSince == None)
  }

  @Test
  def testAddNoneMatchThenAddALL() {
    val tag = new Tag("124", false)
    val conditionals = Conditionals().addIfNoneMatch(tag).addIfNoneMatch(Tag.ALL)
    Assert.assertTrue("Match was not empty", conditionals.ifMatch.isEmpty)
    Assert.assertFalse("None match was empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertEquals("NoneMatch had wrong size", 1, conditionals.ifNonMatch.length)
    Assert.assertEquals("NoneMatch was not equal", List(Tag.ALL), conditionals.ifNonMatch)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == None)
    Assert.assertTrue("un-modified since was not 'None'", conditionals.ifUnModifiedSince == None)
  }

  @Test
  def testAddNoneMatchModifiedSince() {
    val tag = new Tag("124", false)
    val dateOption = Some(new DateTime())
    val conditionals = Conditionals().addIfNoneMatch(tag).setModifiedSince(dateOption)
    Assert.assertTrue("Match was not empty", conditionals.ifMatch.isEmpty)
    Assert.assertFalse("None match was empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertEquals("NoneMatch had wrong size", 1, conditionals.ifNonMatch.length)
    Assert.assertEquals("NoneMatch was not equal", List(tag), conditionals.ifNonMatch)
    Assert.assertTrue("modified since was not correct", conditionals.ifModifiedSince == dateOption)
    Assert.assertTrue("un-modified since was not 'None'", conditionals.ifUnModifiedSince == None)
  }

  @Test
  def testAddNoneMatchUnModifiedSince() {
    val tag = new Tag("124", false)
    val dateOption = Some(new DateTime())
    val conditionals = Conditionals().addIfNoneMatch(tag).setUnModifiedSince(dateOption)
    Assert.assertTrue("Match was not empty", conditionals.ifMatch.isEmpty)
    Assert.assertTrue("None match was not empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == None)
    Assert.assertTrue("un-modified since was not correct", conditionals.ifUnModifiedSince == dateOption)
  }

  @Test
  def testAddNoneMatchUnModifiedSinceHeaders() {
    val tag = new Tag("124", false)
    val dateOption = Some(new DateTime())
    val conditionals = Conditionals().addIfNoneMatch(tag).setModifiedSince(dateOption)    
    Assert.assertEquals("Headers did not have the correct number", 2, conditionals.toHeaders.size)
  }

  @Test
  def testAddMatchUnModifiedSince() {
    val tag = new Tag("124", false)
    val dateOption = Some(new DateTime())
    val conditionals = Conditionals().addIfMatch(tag).setUnModifiedSince(dateOption)
    Assert.assertFalse("Match was not empty", conditionals.ifMatch.isEmpty)
    Assert.assertTrue("None match was not empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == None)
    Assert.assertTrue("un-modified since was not correct", conditionals.ifUnModifiedSince == dateOption)
  }

  @Test
  def testAddMatchModifiedSince() {
    val tag = new Tag("124", false)
    val dateOption = Some(new DateTime())
    val conditionals = Conditionals().addIfMatch(tag).setModifiedSince(dateOption)
    Assert.assertTrue("Match was not empty", conditionals.ifMatch.isEmpty)
    Assert.assertTrue("None match was not empty",conditionals.ifNonMatch.isEmpty)
    Assert.assertTrue("modified since was not 'None'", conditionals.ifModifiedSince == dateOption)
    Assert.assertTrue("un-modified since was not correct", conditionals.ifUnModifiedSince == None)
  }
}