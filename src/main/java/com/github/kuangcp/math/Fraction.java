package com.github.kuangcp.math;

/**
 * Created by Myth on 2017/3/21
 * 将浮点数转换成分数，并提供相关操作方法 貌似是不可行的
 * 最多是给定一个0.3（3）指定循环部分可以求出分数
 * 使用两个字符串分别表示分子和分母来计算,提供加减乘除以及化简的方法
 *
 *
 * 使用Integer就确定了是最长11位的整数长度
 * 改成数值了，还使用符号的标记是为了啥，真是可以了。
 */
public class Fraction {

  private Integer numerator;
  private Integer denominator;

  private Fraction() {
  }

  /**
   * init a new object from other fraction
   */
  public Fraction(Fraction fraction) {
    this.numerator = fraction.getNumerator();
    this.denominator = fraction.getDenominator();
  }

  public Fraction(Integer numerator, Integer denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  /**
   * init a integer
   */
  public Fraction(Integer numerator) {
    this.numerator = numerator;
    this.denominator = 1;
  }

  /**
   * init a fraction by string  ag: -12.3434
   */
  public static Fraction valueOf(String num) {
    int numerator, denominator, minus;
    String[] numberArray = num.split("\\.");

    Fraction fraction = new Fraction();
    if (num.startsWith("-")) {
      minus = -1;
      if (numberArray.length == 2) {
        numerator = Integer.parseInt(numberArray[0].substring(1));
        denominator = Integer.parseInt(numberArray[1]);
        Double dd = Math.pow(10.0, numberArray[1].length());
        Integer temp = dd.intValue();
        fraction.setNumerator(minus * numerator * temp + denominator);
        fraction.setDenominator(temp);
      } else {
        numerator = Integer.parseInt(num.substring(1));
        fraction.setNumerator(minus * numerator);
        fraction.setDenominator(1);
      }
      return fraction.reductionOfFraction();
    }

    if (numberArray.length == 2) {
      numerator = Integer.parseInt(numberArray[0]);
      denominator = Integer.parseInt(numberArray[1]);
      Double dd = Math.pow(10.0, numberArray[1].length());
      Integer temp = dd.intValue();
      fraction.setNumerator(numerator * temp + denominator);
      fraction.setDenominator(temp);
    } else {
      fraction.setNumerator(Integer.parseInt(num));
      fraction.setDenominator(1);
    }
    return fraction.reductionOfFraction();
  }

  /**
   * 加法运算
   */
  public Fraction add(Fraction other) {
    this.reductionOfFraction();
    other.reductionOfFraction();
    Fraction result;

    if (this.getDenominator().equals(other.getDenominator())) {
      Integer numerator = this.getNumerator() + other.getNumerator();
      result = new Fraction(numerator, this.getDenominator());
    } else {
      Integer numerator = this.getNumerator() * other.getDenominator()
          + this.getDenominator() * other.getNumerator();
      result = new Fraction(numerator, this.getDenominator() * other.getDenominator());
    }
    return result.reductionOfFraction();
  }

  public Fraction multiply(Fraction other) {
    Fraction result = new Fraction();
    if (this.getNumerator() == 0 || other.getNumerator() == 0) {
      return result.changeToZero();
    }

    if (this.getDenominator() == 0 || other.getDenominator() == 0) {
      return result.changeToInfinity();
    }

    result.setNumerator(this.getNumerator() * other.getNumerator());
    result.setDenominator(this.getDenominator() * other.getDenominator());

    return result.reductionOfFraction();
  }

  public Fraction multiply(Integer other) {
    Fraction temp = new Fraction(other);
    return multiply(temp);
  }

  public Fraction subtract(Fraction other) {
    Fraction fraction = new Fraction(other);
    fraction.setNumerator(-1 * fraction.getNumerator());
    return add(fraction);
  }

  public Fraction divide(Fraction other) {
    Fraction result = new Fraction(other);
    if (this.isZero()) {
      return result.changeToZero();
    }

    if (this.isInfinity() || other.isInfinity() || other.isZero()) {
      return result.changeToInfinity();
    }
    result = reverseNumeratorAndDenominator(other);
    return multiply(result);
  }

  public boolean isMoreThan(Fraction other) {
    return this.subtract(other).isGreaterThanZero();
  }

  /**
   * 化简函数 使用辗转相除来求最小公约数进行化简 TODO 优化
   */
  public Fraction reductionOfFraction() {
    if (this.isInfinity() || this.isZero() || this.isOne()) {
      return this;
    }
    Integer D, N;
    D = Math.abs(this.getDenominator());
    N = Math.abs(this.getNumerator());

    Integer temp;
    //辗转相除来计算公约数
    while (D != 0) {
      temp = N % D;
      N = D;
      D = temp;
    }
    if (N > 0) {
      int tempD = 1;
      if (this.getDenominator() < 0) {
        tempD = -1;
      }
      this.setDenominator(tempD * this.getDenominator() / N);
      this.setNumerator(tempD * this.getNumerator() / N);
    }
    return this;
  }

  public boolean isGreaterThanZero() {
    this.reductionOfFraction();
    if (this.isInfinity() || this.isZero()) {
      return false;
    }
    return this.getNumerator() > 0;
  }

  public boolean isZero() {
    if (this.isInfinity()) {
      return false;
    }
    return this.getNumerator() == 0;
  }

  public boolean isOne() {
    this.reductionOfFraction();
    return this.getNumerator() == 1 && this.getDenominator() == 1;
  }

  public boolean isInfinity() {
    return this.getDenominator() == 0;
  }

  public Fraction changeToZero() {
    this.setNumerator(0);
    this.setDenominator(1);
    return this;
  }

  public Fraction changeToInfinity() {
    this.setNumerator(0);
    this.setDenominator(0);
    return this;
  }

  public Integer getNumerator() {
    return numerator;
  }

  public void setNumerator(Integer numerator) {
    this.numerator = numerator;
  }

  public Integer getDenominator() {
    return denominator;
  }

  public void setDenominator(Integer denominator) {
    this.denominator = denominator;
  }

  @Override
  public String toString() {
    if (1 == denominator) {
      return numerator + " ";
    } else if (0 == denominator) {
      return "Infinity";
    } else {
      return "" + numerator + "/" + denominator + " ";
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Fraction fraction = (Fraction) o;

    if (!numerator.equals(fraction.numerator)) {
      return false;
    }
    return denominator.equals(fraction.denominator);
  }

  @Override
  public int hashCode() {
    int result = numerator.hashCode();
    result = 31 * result + denominator.hashCode();
    return result;
  }

  private Fraction reverseNumeratorAndDenominator(Fraction target) {
    Fraction result = new Fraction();
    Integer temp = target.getDenominator();
    result.setDenominator(target.getNumerator());
    result.setNumerator(temp);
    return result;
  }

}