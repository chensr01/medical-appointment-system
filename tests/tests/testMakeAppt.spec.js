import { test, expect } from '@playwright/test';
import { loadConfig, login } from './utils';

const { baseUrl, users } = loadConfig();
const user = users[0];


// In this test, a user log in, makes an appointment, and then deletes that appointment
test('testMakeApptPos', async ({ page }) => {
  const slotTime = 'Time: 2021-12-01 08:30AM - 09:00AM';
  await page.goto(baseUrl);

  // Login
  await login(page, user);

  // View available slots and selct one
  await page.getByRole('link', { name: 'Make Appointments' }).click();
  page.once('dialog', async dialog => { 
    // Assert the alert text
    expect(dialog.message()).toBe('Slot Booked Successfully!');
    // Dismiss the alert
    await dialog.accept();
  });

  // Make appointment
  await page.getByRole('button', { name: slotTime }).click();
  // Assert the slot disappeared
  await expect(page.getByRole('button', { name: slotTime })).toHaveCount(0);
  // Assert the appointment shows in MyAppointment
  await page.getByText(user.name).click();
  await expect(page.getByRole('heading', { name: slotTime })).toHaveCount(1);

  // Delete appointment
  const cardElement = await page.locator(`text=${slotTime}`).locator('xpath=ancestor::div[contains(@class, "MuiPaper-root")]');
  await cardElement.locator('button[aria-label="delete"]').click();
  // Assert the appointment disappeared
  await expect(page.getByText(slotTime)).toHaveCount(0);
  // Assert the time slot is available again
  await page.getByRole('banner').getByRole('button').first().click();
  await page.getByRole('link', { name: 'Make Appointments' }).click();
  await expect(page.getByRole('heading', { name: slotTime })).toHaveCount(1);
});