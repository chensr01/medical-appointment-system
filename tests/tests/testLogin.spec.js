import { test, expect } from '@playwright/test';
import { loadConfig, login } from './utils';

const { baseUrl, users } = loadConfig();
const user = users[0];

// Test login with correct name and email
test('testLoginPos', async ({ page }) => {
  await page.goto(baseUrl);

  // Login
  // await login(page, user);
  await page.getByLabel('Name *').click();
	await page.getByLabel('Name *').fill(user.name);
	await page.getByLabel('Email *').click();
	await page.getByLabel('Email *').fill(user.email);
	await page.getByRole('button', { name: 'Sign In' }).click();

  // Home page should dislay user's name and log out button
  await expect(page).toHaveURL(baseUrl + 'home');
  await expect(page.getByText(user.name)).toHaveCount(1);
  await expect(page.getByRole('button', { name: 'Log Out' })).toHaveCount(1);
});