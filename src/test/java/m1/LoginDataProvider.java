package m1;

import org.testng.annotations.DataProvider;

public class LoginDataProvider {

	@DataProvider(name = "loginData")
	public Object[][] getLoginData() {

		return new Object[][] { { "Admin", "admin123" }, // valid
				{ "Admin", "wrongpass" }, // invalid
				{ "WrongUser", "admin123" } //
		};
	}

}
