package com.movie.controller;

import com.movie.model.Cart;
import com.movie.model.Movie;
import com.movie.model.Show;
import com.movie.repository.CartRepository;
import com.movie.repository.MovieRepository;
import com.movie.repository.ShowRepository;
import com.movie.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
public class RestAPIController {

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ShowRepository showRepository;

    @Autowired
    CartRepository cartRepository;


    // Prior to login

    // movies
    @GetMapping("/api/allmovies/")
    public Iterable<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("/api/upcomingmovies/")
    public Iterable<Movie> getAllUpcomingMovies() {
        return movieRepository.findUpcomingMovies();
    }

    @GetMapping("/api/releasedmovies/")
    public Iterable<Movie> getAllReleasedMovies() {
        return movieRepository.findReleasedMovies();
    }

    @GetMapping("/api/findmoviebyid/")
    public String findMoviesById(@RequestParam("id") int id) {
        boolean result = movieRepository.findById(id).isPresent();
        System.out.println("The returned result is" + result);
        if (result == false)
            return "movie id doesn't exist";
        String result2 = movieRepository.findById(id).get().toString();
        return result2;
    }

    @GetMapping("/api/moviebyname/")
    public List<Movie> getMovieByName(@RequestParam("name") String name) {
        return movieRepository.findMovieDetails(name);
    }

    ///////////////////////////////////////////////////////////////
    // shows
    @GetMapping("/api/allshows/")
    public Iterable<Show> getAllShows() {
        return showRepository.findAll();
    }

    @GetMapping("/api/allshowsbycity/")
    public List<Show> getAllShowsByCity(@RequestParam("city") String city) {
        return showRepository.findAllShowsByCity(city);
    }

    ////////////////////////////////////////////////////////////////
    // cities

    @GetMapping("/api/allcities/")
    public List<String> getAllCities() {
        return showRepository.findAllCity();
    }


    /////////////////////////////////////////////////////////////////
    // signup
    @PostMapping("/api/signup/")
    public String PostRegistered(@RequestParam String uname, @RequestParam String password) {
        String result = String.valueOf(userEntityRepository.findUserExist(uname));
        if ((result.equalsIgnoreCase("null"))) {
            userEntityRepository.addUserCredentials(uname, password);
            return uname + ", you successfully registered";
        } else {
            return "user already exists";
        }
    }


    // Post login -- reqs authentication
    @GetMapping("/api/bookmovieshowticket/")
    public String getShowTicket(@RequestParam("username") String uname, @RequestParam("password") String password, @RequestParam("showid") int showId, @RequestParam("quantity") int quantity) {

        String userConfirm = String.valueOf(userEntityRepository.findUserExist(uname));

        if (userConfirm.equalsIgnoreCase("null")) {
            return "User does not exist. Kindly sign up";
        }


        String passwordByUser = String.valueOf(userEntityRepository.findUserPassword(uname));

        if (!(password.equalsIgnoreCase(passwordByUser))) {
            return "Invalid credentials";
        }

        String result = String.valueOf(showRepository.findTicketAvailability(showId, quantity));
        if ((result.equalsIgnoreCase("null"))) {
            return "tickets not available";
        } else {

            int userId = userEntityRepository.findUserIdByName(uname);
            cartRepository.addCartDetails(quantity, showId, userId);
            return quantity + " Tickets added into cart " + showId;
        }

    }


    @GetMapping("/api/cartdetails/")
    public List<Cart> getAllCartDetails(@RequestParam String userName, @RequestParam String password) {

        int userId = userEntityRepository.findUserIdByName(userName);

        String passwordByUser = String.valueOf(userEntityRepository.findUserPassword(userName));

        List<Cart> mine = new ArrayList<Cart>();

        if (!(password.equalsIgnoreCase(passwordByUser))) {
            mine.add(null);
            return mine;
        }

        return cartRepository.findCartDetailsViaUserId(userId);
    }

    @GetMapping("/api/checkout/")
    public String getShowTicket(@RequestParam("username") String uname, @RequestParam("password") String password) {


        String passwordByUser = String.valueOf(userEntityRepository.findUserPassword(uname));

        if (!(password.equalsIgnoreCase(passwordByUser))) {
            return "Invalid credentials";
        }

        int id = userEntityRepository.findUserIdByName(uname);


        List<Cart> cartList = cartRepository.findCartDetailsViaUserId(id);
        int total = cartList.size();
        int showId = cartRepository.findShowIdViaUserId(id);
        int quantity = cartRepository.findQuantityViaUserId(id);

        showRepository.findBooking(showId, quantity);
        cartRepository.deleteEntryviaCartId(id);

        System.out.println("hey all the value is" + toString().valueOf(cartList));
        return "Checked out" + total;
    }


    // Admin
    @PostMapping("/api/addnewmovie/")
    public String PostNewMovie(@RequestParam("username") String uname, @RequestParam("password") String password, @RequestParam("moviename") String moviename, @RequestParam("description") String description, @RequestParam("rating") int rating, @RequestParam("releasedate") Date releaseDate) {

        String authResponse = adminAuthCheck(uname, password);
        if (!authResponse.equals("Valid User")) {
            return "Only Admin can add movies";
        }

        movieRepository.addNewMovies(description, moviename, rating, releaseDate);
        return "movie added successfully";
    }

    @PostMapping("/api/addnewshow/")
    public String AddNewShow(@RequestParam("username") String uname, @RequestParam("password") String password, @RequestParam("availability") int availability, @RequestParam("city") String city, @RequestParam("language") String language, @RequestParam("date") Date date, @RequestParam("movie id") int movieid) {

        String authResponse = adminAuthCheck(uname, password);
        if (!authResponse.equals("Valid User")) {
            return "Only Admin can add new Shows";
        }

        showRepository.addNewShows(availability, city, language, date, movieid);
        return "show added successfully";
    }

    @DeleteMapping("/api/deletemovie/")
    public String PostDeleteMovies(@RequestParam("username") String uname, @RequestParam("password") String password, @RequestParam("moviename") String moviename) {

        String authResponse = adminAuthCheck(uname, password);
        if (!authResponse.equals("Valid User")) {
            return authResponse;
        }

        movieRepository.deleteMoviesByName(moviename);
        return "movie " + moviename + " deleted successfully";
    }

    @PostMapping("/api/deleteashow/")
    public String deleteAShowById(@RequestParam("username") String uname, @RequestParam("password") String password, @RequestParam("showid") int showId) {

        String authResponse = adminAuthCheck(uname, password);
        if (!authResponse.equals("Valid User")) {
            return authResponse;
        }

        boolean result = showRepository.findById(showId).isPresent();
        System.out.println("the returned result is" + result);
        if (result == false)
            return "show id doesn't exist";

        showRepository.deleteById(showId);
        return "Showid " + showId + " successfully deleted by admin";
    }

    @GetMapping("/api/allusers/")
    public List<String> allUsersInfo() {
        return userEntityRepository.findAllUsers();
    }

    @GetMapping("/api/userexist/")
    public Boolean findUserExist(@RequestParam("uname") String name) {
        String result = String.valueOf(userEntityRepository.findUserExist(name));
        if (result.equalsIgnoreCase("null")) {
            return false;
        } else {
            return true;
        }
    }

    private String adminAuthCheck(String userName, String password) {
        //String userConfirm = String.valueOf(userEntityRepository.findUserExist(userName));
        if (!(userName.equalsIgnoreCase("admin"))) {
            //	System.out.println("the value of userconfirm is"+userConfirm);
            return "Only Admin has rights to delete a show";
        }

        String passwordByUser = String.valueOf(userEntityRepository.findUserPassword(userName));

        if (!(password.equalsIgnoreCase(passwordByUser))) {
            return "Invalid admin password";
        }

        return "Valid User";
    }

    private String authCheck(String userName, String password) {

        //TODO logic for auth check for reducing duplication
        return null;
    }

//	@GetMapping("/api/shows/{data}")
//	public List<Shows> shows(@RequestParam MultiValueMap<String, String> params){
//		String language = params.get("AjaxID").get(0);
//		String city = params.get("UserID").get(0);
//
//		List<Shows> pvrs = new ArrayList<Shows>();
//		showsRepository.findAll().forEach(pvrs::add);
//
//		List<Shows> p = new  ArrayList<Shows>();
//		for(int i = 0 ; i < pvrs.size() ; i++){
//			Shows item = pvrs.get(i);
//			if(item.getCity().equalsIgnoreCase(city) && item.getLanguage().equalsIgnoreCase(language)){
//				p.add(item);
//			}
//		}
//		return p;
//	}

//	@GetMapping("/api/movies/{data}")
//    public Iterable<Movies> movies(@RequestParam MultiValueMap<String, String> params){
//		 String language = params.get("AjaxID").get(0);
//		 String city = params.get("UserID").get(0);
//		 System.out.println("-->" + language + "--" + city);
//         return moviesRepository.findAll();
//    }


//	@GetMapping("/api/shows/temp/")
//    public Iterable<Shows> getShow() {
//		System.out.println("@@@@@@@@");
//        return showsRepository.findAll();
//    }
//
//	@GetMapping("/api/movies/temp/")
//    public Iterable<Movies> getTemp(){
//		System.out.println("#################");
//        return moviesRepository.findAll();
//    }

//	@GetMapping("/api/savecart/{data}")
//    public void saveCart(@RequestParam MultiValueMap<String, String> params){
//		 System.out.println("AjaxID: " + params.get("ajaxid"));
//		 System.out.println("UserID: " + params.get("UserID"));
//		 String userId = params.get("ajaxid").get(0);
//		 String showIdStr = params.get("UserID").get(0);
//		 String[] showArr = showIdStr.split("#");
//		 for(int j = 0 ; j < showArr.length ; j++ ) {
//			 System.out.println("-->>" + showArr[j]);
//		 }
//
//		 List<Shoppingcart> allcarts = new ArrayList<Shoppingcart>();
//		 shoppingcartRepository.findAll().forEach(allcarts::add);
//		 List<Shoppingcart> p = new ArrayList<Shoppingcart>();
//
//		int tcartid = 0;
//		int tuserid = 0;
//		int tshowid = 0;
//		int tnumberoftickets = 0;
//		String tcartstring = "";
//
//	     for(int i = 0 ; i < allcarts.size() ; i++) {
//	    	 Shoppingcart item = allcarts.get(i);
//	    		if(item.getUserid() == Integer.parseInt(userId)) {
//	    			tcartid = item.getCartid();
//	    			tuserid = item.getUserid();
//	    			tshowid = item.getShowid();
//	    			tnumberoftickets = item.getNumberoftickets();
//	    			tcartstring = item.getCartstring();
//
//	    			int id = item.getCartid();
//	    			Optional<Shoppingcart> sh = shoppingcartRepository.findById(id);
//	    			shoppingcartRepository.deleteById(sh.get().getCartid());
//	    			shoppingcartRepository.saveAll(shoppingcartRepository.findAll());
//	    		}
//	    	}
//
//	     tcartstring = showIdStr;
//
//	     Shoppingcart sc = new Shoppingcart(tcartid, tuserid, tshowid, tnumberoftickets, tcartstring);
//	     shoppingcartRepository.save(sc);
//	     shoppingcartRepository.saveAll(shoppingcartRepository.findAll());
//    }
//
//	@GetMapping("/api/cartbyuser/")
//    public List<Shoppingcart> showCarts(@RequestParam("userid")int uid){
//
//		 return shoppingcartRepository.findCartDetailsViaUserId(uid);
//    }
//
//	@GetMapping("/api/loadcart/{data}")
//    public String loadCart(@RequestParam MultiValueMap<String, String> params){
//		 System.out.println("AjaxID: " + params.get("ajaxid"));
//		 System.out.println("UserID: " + params.get("UserID"));
//		 String userId = params.get("ajaxid").get(0);
//		 String showIdStr = params.get("UserID").get(0);
//		 return "{id: 1, title: anshul, visible: false},{id: 1, title: abhijeet, visible: false}";
//	}
//
//
//	@GetMapping("/api/loginattempt/{data}")
//    public String loginAttempt(@RequestParam MultiValueMap<String, String> params){
//		System.out.println("UserId: " + params.get("ajaxid"));
//		System.out.println("Password: " + params.get("password"));
//
//		List<Users> allUsers = new ArrayList<Users>();
//		userEntityRepository.findAll().forEach(allUsers::add);
//
//		String userid = params.get("ajaxid").get(0);
//		String password = params.get("password").get(0);
//		boolean isValid = false;
//		System.out.println("-->>" + userid);
//		System.out.println("-->>" + password);
//		int userEntityId = 0;
//
//		for(int j = 0 ; j < allUsers.size() ; j++) {
//			Users obj = allUsers.get(j);
//			System.out.println("################Inside loop--" + obj.getName()+ ";;;"+obj.getPassword());
//
//			if(obj.getName().equalsIgnoreCase(userid) &&
//					obj.getPassword().equals(password)) {
//				System.out.println("Inside loop--");
//				isValid = true;
//				userEntityId = obj.getUserid();
//			}
//		}
//		System.out.println("[{\"status\":\"ok\",\"userEntityId\":" + userEntityId + "}]");
//		if(isValid)
//			return "[{\"status\":\"ok\",\"userEntityId\":" + userEntityId + "}]\"";
//		else
//			return "[{\"status\":\"error\"}]";
//	}


}
