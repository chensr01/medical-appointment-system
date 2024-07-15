const { loadConfig, login } = require('./utils');
const { baseUrl, users } = loadConfig();
const user = users[0];

async function testModifyAppt(page) {
    const slotTime = 'Time: 2021-12-01 08:30AM - 09:00AM';
    const modifySlotTime = 'Time: 2021-12-01 09:00AM - 09:30AM';

    // Login
    await page.goto(baseUrl);
    await login(page, user);

    // Make appointment
    console.log("make appt");
    await page.getByRole('link', { name: 'Make Appointments' }).click();
    page.once('dialog', async dialog => { 
      await dialog.accept();
    });
    await page.getByRole('button', { name: slotTime }).click();

    await page.waitForTimeout(1000);

    // Edit the appointment just made
    console.log("edit appt");
    await page.getByText(user.name).click();
    const cardElement = await page.locator(`text=${slotTime}`).locator('xpath=ancestor::div[contains(@class, "MuiPaper-root")]');
    await cardElement.locator('button[aria-label="edit"]').click();
    page.once('dialog', async dialog => { 
      await dialog.accept();
    });
    await page.getByRole('button', { name: modifySlotTime }).click();

    // Delete appointment
    console.log("delete appt");
    await page.getByText(user.name).click();
    const cardElement2 = await page.locator(`text=${modifySlotTime}`).locator('xpath=ancestor::div[contains(@class, "MuiPaper-root")]');
    await cardElement2.locator('button[aria-label="delete"]').click();
};

// Export the functions
module.exports = {
  testModifyAppt
};
  