const { loadConfig, login } = require('./utils');
const { baseUrl, users } = loadConfig();
const user = users[0];

async function testLoginPos(page) {
  await page.goto(baseUrl);
  await login(page, user);
};

// Export the functions
module.exports = {
  testLoginPos
};
