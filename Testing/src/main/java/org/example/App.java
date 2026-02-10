package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class App {
    public static void main(String[] args) {

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
            );

            Page page = browser.newPage();

            // 1️⃣ Navigate to login page
            page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);

            // 2️⃣ Login
            page.locator("input[name='username']").fill("Admin");
            page.locator("input[name='password']").fill("admin123");
            page.locator("button[type='submit']").click();

            // 3️⃣ Wait for dashboard
            page.waitForLoadState(LoadState.NETWORKIDLE);
            System.out.println("Logged in successfully");

            // =====================================================
            // ✅ ACTION 1: PIM → Add Employee
            // =====================================================

            // Click PIM tab (STRICT-MODE SAFE)
            page.getByRole(AriaRole.LINK,
                    new Page.GetByRoleOptions().setName("PIM")).click();

            // Click Add Employee
            page.getByRole(AriaRole.LINK,
                    new Page.GetByRoleOptions().setName("Add Employee")).click();

            // Assertion: Add Employee header
            Locator addEmployeeHeader = page.locator("h6:has-text('Add Employee')");
            assertThat(addEmployeeHeader).isVisible();

            // Fill employee details
            page.locator("input[name='firstName']").fill("John");
            page.locator("input[name='lastName']").fill("Doe");


            System.out.println("Add Employee page verified and data entered");

            // =====================================================
            // ✅ ACTION 2: Admin tab → Verify table header
            // =====================================================

            page.locator("a[href*='admin']").click();

            // Assertion: Verify System Users header
            Locator systemUsersHeader = page.locator("h5:has-text('System Users')");
            assertThat(systemUsersHeader).isVisible();

            System.out.println("Admin page verified");

            // Pause for visual confirmation
            page.waitForTimeout(8000);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
