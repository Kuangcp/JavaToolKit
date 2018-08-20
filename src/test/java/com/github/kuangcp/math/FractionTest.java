package com.github.kuangcp.math;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-19-上午11:38
 */
public class FractionTest {

  private Fraction fraction = new Fraction(1);

  @Test
  public void testValueOf() {
    Fraction fraction = Fraction.valueOf("12");
    assertThat(fraction, equalTo(new Fraction(12)));

    fraction = Fraction.valueOf("-3.5");
    assertThat(fraction, equalTo(new Fraction(-7, 2)));
  }

  @Test(expected = RuntimeException.class)
  public void testValueOf2() {
    Fraction.valueOf("12.1.1");
  }

  @Test(expected = RuntimeException.class)
  public void testValueOf3() {
    Fraction.valueOf("12.1a");
  }

  @Test(expected = RuntimeException.class)
  public void testValueOf4() {
    Fraction.valueOf("--12.1");
  }

  @Test
  public void testAdd() {
    Fraction result = fraction.add(new Fraction(1, 2));
    assertThat(result, equalTo(new Fraction(3, 2)));

    result = result.add(new Fraction(2, 3));
    assertThat(result, equalTo(new Fraction(13, 6)));
  }

  @Test
  public void testMultiply() {
    Fraction result = fraction.multiply(new Fraction(2, 3));
    assertThat(result, equalTo(new Fraction(2, 3)));

    result = result.multiply(4);
    assertThat(result, equalTo(new Fraction(8, 3)));

    result = result.multiply(3);
    assertThat(result, equalTo(new Fraction(8)));
  }

  @Test
  public void testSubtract() {
    Fraction result = fraction.subtract(new Fraction(2));
    assertThat(result, equalTo(new Fraction(-1)));

    result = result.subtract(new Fraction(2, 0));
    assert result.isInfinity();
  }

  @Test
  public void testDivide() {
    Fraction result = fraction.divide(new Fraction(3));
    assertThat(result, equalTo(new Fraction(1, 3)));

    result = result.divide(new Fraction(3, 0));
    assert result.isInfinity();
  }

  @Test
  public void testIsMoreThan() {
    boolean result = fraction.isMoreThan(3);
    assertThat(result, equalTo(false));

    result = fraction.isMoreThan(new Fraction(1, 5));
    assertThat(result, equalTo(true));
  }

  @Test
  public void testReductionOfFraction() {
    Fraction result = fraction.add(new Fraction(1893, -21)).reductionOfFraction();
    assertThat(result, equalTo(new Fraction(-624, 7)));
  }
}