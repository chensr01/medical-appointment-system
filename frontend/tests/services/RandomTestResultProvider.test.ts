import { TestResultProvider } from "../../src/services/contracts/HealthAdminService";
import { RandomTestResultProvider } from "../../src/services/implementations/RandomTestResultProvider";

const provider: TestResultProvider = new RandomTestResultProvider();

describe('test getTestResult', () => {
  it('should return the same result the same test all the times', async () => {
    const testId = 'DEB33B30-D041-41DD-8AAC-068C554A20A9';
    const result = await provider.getTestResult(testId);
    for (let i = 0; i < 10; i++) {
      const newResult = await provider.getTestResult(testId);
      expect(newResult).toEqual(result);
    }
  });
});
