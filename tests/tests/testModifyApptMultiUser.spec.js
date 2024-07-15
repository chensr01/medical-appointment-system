import { test, expect } from '@playwright/test';
import { loadConfig, login } from './utils';

const { baseUrl, users } = loadConfig();
const user1 = users[0];
const user2 = users[1];

// In this test, 
test('testModifyApptMultiUser', async ({ browser }) => {
    const slotTime = 'Time: 2021-12-01 08:00AM - 08:30AM';
    const modifySlotTime = 'Time: 2021-12-01 08:30AM - 09:00AM';

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
    
    // User1 edits an appointment
    await page1.getByRole('link', { name: 'My Appointments' }).click();

    const cardElement1 = await page1.locator(`text=${slotTime}`).locator('xpath=ancestor::div[contains(@class, "MuiPaper-root")]');
    await cardElement1.locator('button[aria-label="edit"]').click();
    await page1.getByRole('button', { name: modifySlotTime }).click();

    page1.once('dialog', async dialog => { 
        // Assert the alert text
        expect(dialog.message()).toBe('Slot Updated Successfully!');
        // Dismiss the alert
        await dialog.accept();
    });

    // Assert the slot disappeared
    await expect(page1.getByRole('button', { name: slotTime })).toHaveCount(0);
    // Assert the appointment shows in MyAppointment
    await page1.getByText(user1.name).click();
    await expect(page1.getByRole('heading', { name: modifySlotTime })).toHaveCount(1);


    // User2 makes an appointment and handles the dialog
    await page2.getByRole('link', { name: 'Make Appointments' }).click();
    page2.once('dialog', async dialog => {
        expect(dialog.message()).toBe('Slot Booked Successfully!');
        await dialog.accept();
    });
    await page2.getByRole('button', { name: slotTime }).click();
    await page2.getByText(user2.name).click();
    await expect(page2.getByRole('heading', { name: modifySlotTime })).toHaveCount(0);
    await expect(page2.getByRole('heading', { name: slotTime })).toHaveCount(1);

    // User2 delete that appointment
    const cardElement3 = await page2.locator(`text=${slotTime}`).locator('xpath=ancestor::div[contains(@class, "MuiPaper-root")]');
    await cardElement3.locator('button[aria-label="delete"]').click();
    // Assert the appointment disappeared
    await expect(page2.getByText(slotTime)).toHaveCount(0);

    const cardElement2 = await page1.locator(`text=${modifySlotTime}`).locator('xpath=ancestor::div[contains(@class, "MuiPaper-root")]');
    await cardElement2.locator('button[aria-label="edit"]').click();

    // Make appointment back to original version
    await page1.getByRole('button', { name: slotTime }).click();

    page1.once('dialog', async dialog => { 
        // Assert the alert text
        expect(dialog.message()).toBe('Slot Updated Successfully!');
        // Dismiss the alert
        await dialog.accept();
    });
    // Assert the slot exists
    await expect(page1.getByRole('button', { name: slotTime })).toHaveCount(1);
    
});
