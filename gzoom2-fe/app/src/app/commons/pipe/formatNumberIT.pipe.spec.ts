import { FormatNumberITPipe } from './formatNumberIT.pipe';

describe('FormatNumberITPipe', () => {
  let pipe: FormatNumberITPipe;

  beforeEach(() => {
    pipe = new FormatNumberITPipe();
  });

  it('should format integer correctly', () => {
    expect(pipe.transform(1234567)).toBe('1.234.567');
  });

  it('should format decimal with comma', () => {
    expect(pipe.transform(1234.56)).toBe('1.234,56');
  });

  it('should limit decimals to maxDecimals', () => {
    expect(pipe.transform(1234.55789, 2)).toBe('1.234,56');
  });

  it('should trim trailing zeroes', () => {
    expect(pipe.transform(1234.5000, 4)).toBe('1.234,5');
  });

  it('should handle string numbers', () => {
    expect(pipe.transform('100000.190')).toBe('100.000,19');
  });

  it('should ignore non-numeric strings', () => {
    expect(pipe.transform('not-a-number')).toBe('not-a-number');
  });

  it('should return null if undefined', () => {
    expect(pipe.transform(undefined)).toBeNull();
  });

  it('should return null if null', () => {
    expect(pipe.transform(null)).toBeNull();
  });

  it('should format negative numbers', () => {
    expect(pipe.transform(-98765.4321, 3)).toBe('-98.765,432');
  });
});
