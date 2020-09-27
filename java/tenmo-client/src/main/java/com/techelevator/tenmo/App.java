package com.techelevator.tenmo;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import com.techelevator.tenmo.services.TransferServices;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
			MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS,
			MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private AccountService accountService = new AccountService(API_BASE_URL);
	private TransferServices transferServices = new TransferServices(API_BASE_URL);
	private UserService userService = new UserService(API_BASE_URL);

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;

	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		System.out.println("Your current account balance is: $" + accountService.getAccountBalance());
	}

	private void viewTransferHistory() {
		displayTransferSummaryBanner();
		displayTransferSummary();
		String choice = (String) console.getUserInput("\nPlease enter transfer ID to view details (0 to cancel): " );
		while (!choice.equals("0")) {
			displayTransferDetails(transferServices.getTransferById(Long.parseLong(choice)));
			choice = (String) console.getUserInput("\nPlease enter another transfer ID to view details (0 to cancel): " );
		}
	}

	private void displayTransferSummaryBanner() {
		System.out.println("----------------------------------------------------");
		System.out.println("Transfers");
		System.out.printf("%-8s %-15s %-15s %-1s", "ID", "From", "To", "Amount\n");
		System.out.println("----------------------------------------------------");
	}

	private void displayTransferSummary() {
		for (Transfer thisTransfer : transferServices.historyOfTransfers()) {
			System.out.printf("%-8s %-15s %-15s %-1s", thisTransfer.getTransferId(),
					userService.getUserById((thisTransfer.getUserFrom())).getUsername(),
					userService.getUserById(thisTransfer.getUserTo()).getUsername(), 
					thisTransfer.getAmount() +"\n");
		}

	}

	private void displayTransferDetails(Transfer transfer) {
		System.out.println(
				"\nTransfer Id = " + transfer.getTransferId() 
				+ "\nFrom Account = " + userService.getUserById((transfer.getUserFrom())).getUsername()
				+ "\nTo Account = " + userService.getUserById(transfer.getUserTo()).getUsername() 
				+ "\nTransfer Type = " + transfer.getTransferTypeDesc() 
				+ "\nTransfer Status = " + transfer.getTransferStatusDesc()
				+ "\nTransfer Amount = " + transfer.getAmount());
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub

	}

	private void sendBucks() {
		Long userToId = null;
		BigDecimal transferAmount = null;
		displayRegisteredUsers();
		String choice = (String) console.getUserInput("\nEnter ID of user you are sending to (0 to cancel): " );
		while (!choice.equals("0")) {
		userToId = (Long.parseLong(choice));
		choice = (String) console.getUserInput("\nEnter amount");
		transferAmount = (new BigDecimal(choice).setScale(2, RoundingMode.HALF_UP));
		System.out.println(transferServices.sendTransfer(userToId, transferAmount));
		choice = (String) console.getUserInput("\nEnter another ID of user if you would like to send more TE Bucks (0 to cancel): " );
		}
	}

	private void displayRegisteredUsers() {
		List<User> userList = transferServices.getRegisteredUsers();
		System.out.println("---------------");
		System.out.printf("%-5s %-1s", "Id", "User\n");
		System.out.println("---------------");
		for (User thisUser : userList) {
			System.out.printf("%-5s %-1s",
					thisUser.getId(), thisUser.getUsername() + "\n");
		}
	}
	
	private void requestBucks() {
		// TODO Auto-generated method stub

	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
				this.accountService.setToken(currentUser.getToken());
				this.transferServices.setToken(currentUser.getToken());
				this.userService.setToken(currentUser.getToken());
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
