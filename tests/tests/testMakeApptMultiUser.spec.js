import { test, expect } from '@playwright/test';
import { loadConfig, login } from './utils';

const { baseUrl, users } = loadConfig();
const user1 = users[0];
const user2 = users[1];

// In this test, user1 and user2 log in, and they both go to the make appointment page
// User2 makes an appointment, and after that, user1 tries to make that same appointment
// Then, we check that user1 did not make that appointment successfully
test('testMultipleAppts', async ({ browser }) => {
  const slotTime = 'Time: 2021-12-01 08:30AM - 09:00AM';

  // Load the configuration
  const context1 = await browser.newContext();
  const context2 = await browser.newContext();

  // Create the first page for Bilbo Baggins
  const page1 = await context1.newPage();
  await page1.goto(baseUrl);
  await login(page1, user1);

  // Create the second page for Meriadoc Brandybuck
  const page2 = await context2.newPage();
  await page2.goto(baseUrl);
  await login(page2, user2);
  
  // User1 makes an appointment
  await page1.getByRole('link', { name: 'Make Appointments' }).click();

  // User2 makes an appointment and handles the dialog
  await page2.getByRole('link', { name: 'Make Appointments' }).click();
  page2.once('dialog', async dialog => {
    expect(dialog.message()).toBe('Slot Booked Successfully!');
    await dialog.accept();
  });
  await page2.getByRole('button', { name: slotTime }).click();

  // User1 checks his appointment
  await page1.getByRole('button', { name: slotTime }).click();
  await page1.getByRole('button', { name: slotTime }).click();
  await page1.getByText(user1.name).click();
  await expect(page1.getByRole('heading', { name: slotTime })).toHaveCount(0);

  // User2 deletes appointment just made
  await page2.getByText(user2.name).click();
  const cardElement = await page2.locator(`text=${slotTime}`).locator('xpath=ancestor::div[contains(@class, "MuiPaper-root")]');
  await cardElement.locator('button[aria-label="delete"]').click();
});
