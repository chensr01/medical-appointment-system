import * as fs from 'fs';
import * as path from 'path';

// Load configuration
export function loadConfig() {
	const configPath = path.resolve(__dirname, '../config.json');
	const configFile = fs.readFileSync(configPath, 'utf8');
	return JSON.parse(configFile);
}

// Login
export async function login(page, user) {
	await page.getByLabel('Name *').click();
	await page.getByLabel('Name *').fill(user.name);
	await page.getByLabel('Email *').click();
	await page.getByLabel('Email *').fill(user.email);
	await page.getByRole('button', { name: 'Sign In' }).click();
}