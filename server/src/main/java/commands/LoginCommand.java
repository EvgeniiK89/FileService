package commands;

import users.UserHandler;

public class LoginCommand implements Command{
    @Override
    public String getCommandName() {
        return "login";
    }

    @Override
    public String executeCommand(String params) { // "username password" -> ["username", "passord"]
        String[] s = params.trim().split(" ");
        String userName = s[0];
        if (UserHandler.findUserByUsername(userName)) {
            String password = s[1];
            if (UserHandler.checkPassword(userName, password)) {
                return "success";
            }
        }
        return "invalid login/password!";
    }
}
