package com.github.kuangcp.math;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-19-上午11:38
 */
public class FractionTest {

  private Fraction fraction = new Fraction(1);

  @Test
  public void testAdd() {
    Fraction result = fraction.add(new Fraction(1, 2));
    assertThat(result, equalTo(new Fraction(3, 2)));

    result = result.add(new Fraction(2, 3));
    assertThat(result, equalTo(new Fraction(13,6)));
  }

  @Test
  public void testMultiply() throws Exception {
    Fraction result = fraction.multiply(new Fraction(0, 0));
    Assert.assertEquals(new Fraction(0, 0), result);
  }

  @Test
  public void testMultiply2() throws Exception {
    Fraction result = fraction.multiply(0);
    Assert.assertEquals(new Fraction(0, 0), result);
  }

  @Test
  public void testSubtract() throws Exception {
    Fraction result = fraction.subtract(new Fraction(0, 0));
    Assert.assertEquals(new Fraction(0, 0), result);
  }

  @Test
  public void testDivide() throws Exception {
    Fraction result = fraction.divide(new Fraction(0, 0));
    Assert.assertEquals(new Fraction(0, 0), result);
  }

  @Test
  public void testIsMoreThan() throws Exception {
    boolean result = fraction.isMoreThan(new Fraction(0, 0));
    Assert.assertEquals(true, result);
  }

  @Test
  public void testReductionOfFraction() throws Exception {
    Fraction result = fraction.reductionOfFraction();
    Assert.assertEquals(new Fraction(0, 0), result);
  }

  @Test
  public void testGreaterThanZero() throws Exception {
    boolean result = fraction.isGreaterThanZero();
    Assert.assertEquals(true, result);
  }

  @Test
  public void testIsZero() throws Exception {
    boolean result = fraction.isZero();
    Assert.assertEquals(true, result);
  }

  @Test
  public void testIsOne() throws Exception {
    boolean result = fraction.isOne();
    Assert.assertEquals(true, result);
  }

  @Test
  public void testIsInfinity() throws Exception {
    boolean result = fraction.isInfinity();
    Assert.assertEquals(true, result);
  }

  @Test
  public void testToString() throws Exception {
    String result = fraction.toString();
    Assert.assertEquals("replaceMeWithExpectedResult", result);
  }
}