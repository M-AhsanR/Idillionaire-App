package com.idillionaire.app.ApiStructure;

public interface Constants {

        interface URL {
            String BASE_URL = "http://50.116.22.10:4100";

            String LOGIN = BASE_URL + "/user/login";
            String LOGINFB = BASE_URL + "/user/login_with_facebook?access_token=";
            String REGISTER = BASE_URL + "/user/register";
            String HOME = BASE_URL + "/fetch_Data/fetch_category";
            String ADD_FAVRT = BASE_URL + "/user/add_favourite/";
            String FVRT = BASE_URL + "/user/list_favourite";
            String RMVFVRT = BASE_URL + "/user/remove_favourite/";
            String FORGOTPASS = BASE_URL + "/user/forgot/password/email_pin";
            String RESETPASS = BASE_URL + "/user/reset/password/with/pin";
            String CHANGEPASS = BASE_URL + "/user/change/password";
            String EDITPROFILE = BASE_URL + "/user/update/profile";
            String LISTGRATITUDE = BASE_URL + "/user/list_gratitude";
            String REMOVEGRATITUDE = BASE_URL + "/user/remove_gratitude/";
            String LISTMANIFESTATION = BASE_URL + "/user/list_manifestation";
            String REMOVEMANIFESTATION = BASE_URL + "/user/remove_manifestation/";
            String ADDMANIFESTATION = BASE_URL + "/user/add_manifestation/";
            String UPDATEMANIFESTATION = BASE_URL + "/user/update_manifestation/";
            String UPDATEGRATITUDE = BASE_URL + "/user/update_gratitude/";
            String ADDGRATITUDE = BASE_URL + "/user/add_gratitude";
            String GETAPPPREFERENCES = BASE_URL + "/user/get_preferences";
            String REPORTPROBLEM = BASE_URL + "/user/report";
            String BOOKCLUB = BASE_URL + "/user/book_club";
            String JOURNAL = BASE_URL + "/user/get_journal_images";
            String BOOKDETAIL = BASE_URL + "/user/get_book?book_id=";
            String LOGOUT = BASE_URL + "/user/logout";

            // refresh token
            String UPDATE_TOEKN = BASE_URL + "/user/update/token?token=";
            String SESSION_ID = "&session_id=";

            // Get gallery

            String GETGALLERY = BASE_URL + "/user/get_gallery?gallery_id=";

            // Twitter Login
            String TWITTERLOGIN = BASE_URL + "/user/login_with_twitter?access_token=";
            String SECRET = "&access_token_secret=";
            String CONSUMERKEY = "&consumer_key=";
            String CONSUMERSECRET = "&consumer_secret=";

            // Google sign in

            String GOOGLELOGIN = BASE_URL + "/user/login_with_google?access_token=";
            String FNAME = "&first_name=";
            String LNAME = "&last_name=";

        }
}
