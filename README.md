# 		    			Web应用开发技术

## 				实验一 基于MVC用JSP/Servlet实现JPetStore

#### 一、实验目的---所完成的任务(三个基础任务以及两个扩展任务)

1. ##### 商品展示业务模块，包括大类Category、小类Product和具体商品Item的展示和搜索功能。

2. ##### 用户管理业务模块，包括用户注册、登录、修改用户信息、查询用户相关订单等业务功能。

3. ##### 订单管理模块，包括购物车管理、新增订单、地址信息等业务功能。

4. ##### 添加验证码功能：在用户注册和用户登录模块中添加验证码功能。

#### 二、试验步骤及代码展示

##### 1、 商品展示业务模块，包括大类Category、小类Product和具体商品Item的展示和搜索功能。

###### 1.1大类Category

> 大类Category先实现Category.jsp进行大类的具体展示，利用jsp中c:foreach语法对每个大类进行展示，具体类的ID利用a标签进行跳转到小类进行查看，接下来实现domain层的Category.java，完成大类的基础设计，接下来实现persistence中的数据库连接层，先写DAO定义所有接口，再写实现的Implement方法，对数据库进行操作；再写一个Category的Service类，可以在Servlet层中调用该类的相关方法进行进一步操作，最后完成ViewCategoryServlet对jsp页面与业务层进行连接跳转。

完成Control层ViewCategoryServlet:

```java
public class ViewCategoryServlet extends HttpServlet {
    private static final String VIEW_CATEGORY = "/WEB-INF/jsp/catalog/Category.jsp";
    private String categoryId;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        categoryId = request.getParameter("categoryId");
        CatalogService service = new CatalogService();
        Category category = service.getCategory(categoryId);
        List<Product> productList = service.getProductListByCategory(categoryId);

        //保存数据
        HttpSession session = request.getSession();
        session.setAttribute("category", category);
        session.setAttribute("productList", productList);

        //跳转页面
        request.getRequestDispatcher(VIEW_CATEGORY).forward(request, response);
    }
```

###### 1.2小类Product

> 小类Product先完善Product.jsp，主要是通过productId对产品类进行信息展示，之后再实现Product.java，接下进行ProductDAOImpl数据库的连接进行编写，最后完成Servlet的编写。

控制层Servlet代码

```java
public class ViewProductServlet extends HttpServlet {
    private static final String VIEW_PRODUCT = "/WEB-INF/jsp/catalog/Product.jsp";
    private String productId;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        productId = request.getParameter("productId");
        CatalogService service = new CatalogService();
        Product product = service.getProduct(productId);
        List<Item> itemList = service.getItemListByProduct(productId);

        HttpSession session = request.getSession();
        session.setAttribute("product", product);
        session.setAttribute("itemList", itemList);

        request.getRequestDispatcher(VIEW_PRODUCT).forward(request, response);
    }
}
```

###### 1.3具体商品Item

> 用户点击产品后进入查看具体商品，完善Item.jsp，Copy代码模版中的Item.java，实现ItemDAOImpl的具体数据库连接操作，完成最后的Servlet连接。

控制层的Servlet连接:ViewItemServlet:

```java
public class ViewItemServlet extends HttpServlet {
    private static final String VIEW_ITEM = "/WEB-INF/jsp/catalog/Item.jsp";
    private String itemId;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        itemId = request.getParameter("itemId");
        CatalogService service = new CatalogService();
        Item item = service.getItem(itemId);

        HttpSession session = request.getSession();
        session.setAttribute("item", item);

        request.getRequestDispatcher(VIEW_ITEM).forward(request, response);
    }
}
```

###### 1.4完成搜索功能

> 搜索功能先完善显示的SearchProducts.jsp，接下直接进行SearchProductServlet编写和配置，通过获得输入的关键字再通过CatalogService的方法进行查找并显示。

SearchProductServlet

```java
public class SearchProductServlet extends HttpServlet {
    private static final String SEARCH_PRODUCTS = "/WEB-INF/jsp/catalog/SearchProducts.jsp";

    private String keyword;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        keyword = request.getParameter("keyword");
        //request.setAttribute("keyword", keyword);
        CatalogService service = new CatalogService();
        List<Product> productList = service.searchProductList(keyword);

        //保存数据
        HttpSession session = request.getSession();
        session.setAttribute("keyword", keyword);
        session.setAttribute("productList", productList);

        //跳转页面
        request.getRequestDispatcher(SEARCH_PRODUCTS).forward(request, response);
    }
}
```

##### 2. 用户管理业务模块，包括用户注册、登录、修改用户信息、查询用户相关订单等业务功能。

###### 2.1用户注册

> 用户注册时，需要一个全新的包含用户所有信息的输入界面即NewAccountForm.jsp，之后需要先进行设计NewAccountFormServlet，该Servlet的功能仅为实现用户点击注册后跳转用户输入新账号信息的表格，接下实现Account.java，完成Account的基础功能，之后进行AccountDAO接口定义以及完成AccountDAOImpl连接数据库的实现，接下来完成AccountService的编写，之后进行NewAccountServlet配置，获取网页界面的信息，赋值给一个新账号，再将该账号插入到数据库中。

AccountService:

```java
public class AccountService {

    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAOImpl();
    }

    public Account getAccount(String username) {
        return accountDAO.getAccountByUsername(username);
    }

    public Account getAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return accountDAO.getAccountByUsernameAndPassword(account);
    }

    public void insertAccount(Account account) {
        accountDAO.insertAccount(account);
        accountDAO.insertProfile(account);
        accountDAO.insertSignon(account);
    }

    public void updateAccount(Account account) {
        accountDAO.updateAccount(account);
        accountDAO.updateProfile(account);

        if (account.getPassword() != null && account.getPassword().length() > 0) {
            accountDAO.updateSignon(account);
        }
    }
}
```

NewAccountServlet:

```java
public class NewAccountServlet extends HttpServlet {
    private static final String MAIN = "/WEB-INF/jsp/catalog/Main.jsp";
    private static final String NEWACCOUNTFORM = "/WEB-INF/jsp/account/NewAccountForm.jsp";

    private Account account;
    private Account account1;
    private AccountService accountService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        account = (Account) session.getAttribute("account");
        account = null;
        session.setAttribute("account", account);

        //获得输入的验证码值
        String value1=request.getParameter("vCode");
        /*获取图片的值*/
        String value2=(String)session.getAttribute("checkcode");
        Boolean isSame = false;
        /*对比两个值（字母不区分大小写）*/
        if(value2.equalsIgnoreCase(value1)){
            isSame = true;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zip = request.getParameter("zip");
        String country = request.getParameter("country");
        String languagePreference = request.getParameter("languagePreference");
        String favouriteCategoryId = request.getParameter("favouriteCategoryId");
        String listOption = request.getParameter("listOption");
        String bannerOption = request.getParameter("bannerOption");

        account1 = new Account();
        account1.setUsername(username);
        account1.setPassword(password);
        account1.setFirstName(firstName);
        account1.setLastName(lastName);
        account1.setEmail(email);
        account1.setPhone(phone);
        account1.setAddress1(address1);
        account1.setAddress2(address2);
        account1.setCity(city);
        account1.setState(state);
        account1.setZip(zip);
        account1.setCountry(country);
        account1.setLanguagePreference(languagePreference);
        account1.setFavouriteCategoryId(favouriteCategoryId);
        account1.setListOption(Boolean.parseBoolean(listOption));
        account1.setBannerOption(Boolean.parseBoolean(bannerOption));

        if(isSame){
            accountService = new AccountService();
            accountService.insertAccount(account1);

            if(account1 != null){
            request.getRequestDispatcher(MAIN).forward(request, response);
        }else{
            session.setAttribute("messageAccount", "Invalid Verification Code.");

            if(account1 != null){
            request.getRequestDispatcher(NEWACCOUNTFORM).forward(request, response);
        }
    }
}
```

###### 2.2用户登录

> 用户登录先完成SignonForm.jsp，能够使用户输入登入的账号与密码，接下来先用一个Servlet实现SignOnFormServlet实现点击登录时网页跳转到用户输入账号密码的界面，接下来使用SignOnServlet实现获取用户的账号密码对数据库进行核查，如果存在则登录，反之则给予一定的错误信息提示。

SignOnFormServlet:

```java
public class SignOnFormServlet extends HttpServlet {

    private static final String SIGNONFORM = "/WEB-INF/jsp/account/SignonForm.jsp";

    private Account account;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        account = (Account)session.getAttribute("account");

        request.getRequestDispatcher(SIGNONFORM).forward(request, response);
    }
}
```

SignOnServlet:

```java
public class SignOnServlet extends HttpServlet {
    private static final String MAIN = "/WEB-INF/jsp/catalog/Main.jsp";
    private static final String SIGNONFORM = "/WEB-INF/jsp/account/SignonForm.jsp";

    private Account account;
    private AccountService accountService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        accountService = new AccountService();
        account = accountService.getAccount(username, password);

        HttpSession session = request.getSession();
        session.setAttribute("account", account);

        //获得输入的验证码值
        String value1=request.getParameter("vCode");
        /*获取图片的值*/
        String value2=(String)session.getAttribute("checkcode");
        Boolean isSame = false;
        /*对比两个值（字母不区分大小写）*/
        if(value2.equalsIgnoreCase(value1)){
            isSame = true;
        }

        if (account == null || !isSame){
            if(!isSame){
                session.setAttribute("messageSignOn", "Invalid Verification Code.   Signon failed.");
            }else{
                session.setAttribute("messageSignOn", "Invalid username or password.  Signon failed.");
            }
            session.setAttribute("account", null);
            request.getRequestDispatcher(SIGNONFORM).forward(request, response);
        }else{
            account.setPassword(null);
            request.getRequestDispatcher(MAIN).forward(request, response);
        }
    }
}
```

###### 2.3用户修改信息

> 修改个人账号信息也就是进行个人信息查看，同时可以进行修改，修改后点击Save Account Information即可获取新的用户信息，之后进行数据库信息更新。

EditAccountServlet:

```java
public class EditAccountServlet extends HttpServlet {

    private static final String EDITACOUNT = "/WEB-INF/jsp/account/EditAccountForm.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(EDITACOUNT).forward(request, response);
    }
}
```

SaveAccountServlet:

```java
public class SaveAccountServlet extends HttpServlet {

    private static final String EDITACOUNT = "/WEB-INF/jsp/account/EditAccountForm.jsp";

    private Account account;
    private AccountService accountService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        account = (Account) session.getAttribute("account");

        String username = account.getUsername();
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zip = request.getParameter("zip");
        String country = request.getParameter("country");
        String languagePreference = request.getParameter("languagePreference");
        String favouriteCategoryId = request.getParameter("favouriteCategoryId");
        String listOption = request.getParameter("listOption");
        String bannerOption = request.getParameter("bannerOption");

        account.setUsername(username);
        account.setPassword(password);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setPhone(phone);
        account.setAddress1(address1);
        account.setAddress2(address2);
        account.setCity(city);
        account.setState(state);
        account.setZip(zip);
        account.setCountry(country);
        account.setLanguagePreference(languagePreference);
        account.setFavouriteCategoryId(favouriteCategoryId);
        account.setListOption(Boolean.parseBoolean(listOption));
        account.setBannerOption(Boolean.parseBoolean(bannerOption));

        accountService = new AccountService();
        accountService.updateAccount(account);

        session.setAttribute("account", account);

        request.getRequestDispatcher(EDITACOUNT).forward(request, response);
    }
}
```

###### 2.4查询订单

> 账户订单查看，先完成ListOrders.jsp的实现，接下来实现Order.java的基础模块，创建OrderDAO接口，完成数据库的具体操作OrderDAOImpl，接下来实现OrderService，成功完成业务层调用，最后完成ViewListOrderServlet实现用户可以从数据库中查看到自己的订单信息。

OrderService

```java
public class OrderService {

  private ItemDAO itemDAO;
  private OrderDAO orderDAO;
  private SequenceDAO sequenceDAO;
  private LineItemDAO lineItemDAO;

  public OrderService(){
    itemDAO = new ItemDAOImpl();
    orderDAO = new OrderDAOImpl();
    sequenceDAO = new SequenceDAOImpl();
    lineItemDAO = new LineItemDAOImpl();
  }

  public void insertOrder(Order order) {
    order.setOrderId(getNextId("ordernum"));
    for (int i = 0; i < order.getLineItems().size(); i++) {
      LineItem lineItem = (LineItem) order.getLineItems().get(i);
      String itemId = lineItem.getItemId();
      Integer increment = new Integer(lineItem.getQuantity());
      Map<String, Object> param = new HashMap<String, Object>(2);
      param.put("itemId", itemId);
      param.put("increment", increment);
      itemDAO.updateInventoryQuantity(param);
    }

    orderDAO.insertOrder(order);
    orderDAO.insertOrderStatus(order);
    for (int i = 0; i < order.getLineItems().size(); i++) {
      LineItem lineItem = (LineItem) order.getLineItems().get(i);
      lineItem.setOrderId(order.getOrderId());
      lineItemDAO.insertLineItem(lineItem);
    }
  }

  public Order getOrder(int orderId) {
    Order order = orderDAO.getOrder(orderId);
    order.setLineItems(lineItemDAO.getLineItemsByOrderId(orderId));

    for (int i = 0; i < order.getLineItems().size(); i++) {
      LineItem lineItem = (LineItem) order.getLineItems().get(i);
      Item item = itemDAO.getItem(lineItem.getItemId());
      item.setQuantity(itemDAO.getInventoryQuantity(lineItem.getItemId()));
      lineItem.setItem(item);
    }

    return order;
  }

  public List<Order> getOrdersByUsername(String username) {
    return orderDAO.getOrdersByUsername(username);
  }

  public int getNextId(String name) {
    Sequence sequence = new Sequence(name, -1);
    sequence = sequenceDAO.getSequence(sequence);
    if (sequence == null) {
      throw new RuntimeException("Error: A null sequence was returned from the database (could not get next " + name
          + " sequence).");
    }
    Sequence parameterObject = new Sequence(name, sequence.getNextId() + 1);
    sequenceDAO.updateSequence(parameterObject);
    return sequence.getNextId();
  }

}
```

ViewListServlet:

```java
public class ViewListOrderServlet extends HttpServlet {
    private static final String VIEWLISTORDER = "/WEB-INF/jsp/order/ListOrders.jsp";

    private String username;
    private OrderService orderService;
    private List<Order> orderList = new ArrayList<Order>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        username = request.getParameter("username");
        orderService = new OrderService();
        orderList = orderService.getOrdersByUsername(username);

        HttpSession session = request.getSession();
        session.setAttribute("orderList", orderList);

        //HttpSession session = request.getSession();
        Account account = (Account)session.getAttribute("account");


        request.getRequestDispatcher(VIEWLISTORDER).forward(request, response);
    }
}
```

##### 3. 订单管理模块，包括购物车管理、新增订单、地址信息等业务功能。

###### 3.1添加商品到购物车

> 先实现Cart.jsp，能够在网页显示购物车栏目信息，接下来具体实现Cart.java使得用户可以创建Cart类，最后创建AddItemToCartServlet，当用户在浏览商品界面时，点击Add to Cart时可以将该商品加入到购物车中并显示相关信息。

AddItemToCartServlet

```java
public class AddItemToCartServlet extends HttpServlet {

    //Servlet的功能即负责中转
    //1.处理完请求后的跳转页面
    private static final String VIEW_CART = "/WEB-INF/jsp/cart/Cart.jsp";

    //2.定义处理该请求所需要的数据
    private String workingItemId;
    private Cart cart;             //购物车

    //3.是否需要调用业务逻辑层
    private CatalogService catalogService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        workingItemId = request.getParameter("workingItemId");

        Account account;
        //从对话中，获取购物车
        HttpSession session = request.getSession();
        cart = (Cart)session.getAttribute("cart");
        account = (Account)session.getAttribute("account");

        if(cart == null){
            //第一次使用购物车
            cart = new Cart();
        }

        if(cart.containsItemId(workingItemId)){
            //已有该物品，数量加一
            cart.incrementQuantityByItemId(workingItemId);

        }else{
            catalogService = new CatalogService();
            boolean isInStock = catalogService.isItemInStock(workingItemId);
            Item item = catalogService.getItem(workingItemId);
            cart.addItem(item, isInStock);
            session.setAttribute("cart", cart);

            request.getRequestDispatcher(VIEW_CART).forward(request, response);
        }
    }
}
```

###### 3.2更改购物车商品的数量信息

> 在购物车的数量栏输入想要更改后的商品数量，点击Update Cart即可更新购物车中的商品数量、总价。点击之后利用UpdateCartQuantitiesServlet实现信息更新以及页面更新，传递商品的数量信息获取后进行相关操作。

UpdateCartQuantitiesServlet：

```java
public class UpdateCartQuantitiesServlet extends HttpServlet {

    private static final String VIEW_CART = "/WEB-INF/jsp/cart/Cart.jsp";

    private String workingItemId;
    private Cart cart;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        workingItemId = request.getParameter("workingItemId");
        CatalogService catalogService = new CatalogService();

        //从对话中，获取购物车
        HttpSession session = request.getSession();
        cart = (Cart)session.getAttribute("cart");

        Iterator<CartItem> cartItemIterator = cart.getAllCartItems();

        while (cartItemIterator.hasNext()){
            //产品数量增加
            CartItem cartItem = (CartItem)cartItemIterator.next();
            String itemId = cartItem.getItem().getItemId();

            try {
                int quantity = Integer.parseInt((String) request.getParameter(itemId));
                cart.setQuantityByItemId(itemId, quantity);
                if (quantity < 1) {
                    cartItemIterator.remove();
                }
            } catch (Exception e) {
                //ignore parse exceptions on purpose
                e.printStackTrace();
            }

            //CartItem cartItem = cartItemIterator.next();
            //cartItem.incrementQuantity();
        }

        session.setAttribute("cart", cart);

        request.getRequestDispatcher(VIEW_CART).forward(request, response);
    }
}
```

3.3移除购物车中的商品

> 点击购物车中Remove按钮，获取购物车中的信息，之后进入RemoveItemFromCartServlet进行删除操作，并更新该购物车中的信息，如果误操作，则提示该误操作信息。

RemoveItemFromCartServlet：

```java
public class RemoveItemFromCartServlet extends HttpServlet {

    private static final String VIEW_CART = "/WEB-INF/jsp/cart/Cart.jsp";
    private static final String ERROR = "/WEB-INF/jsp/common/Error.jsp";

    private String workingItemId;
    private Cart cart;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        workingItemId = request.getParameter("workingItemId");

        HttpSession session = request.getSession();
        cart = (Cart)session.getAttribute("cart");

        Item item = cart.removeItemById(workingItemId);

        if(item == null) {
            session.setAttribute("message", "Attempted to remove null CartItem from Cart.");
            request.getRequestDispatcher(ERROR).forward(request, response);
        }else{
            request.getRequestDispatcher(VIEW_CART).forward(request, response);
        }
    }
}
```

###### 3.4新增订单——跳转到查看寄件人与收件人地址

> 生成订单界面，包括修改地址信息时共有四个页面，第一个页面是购买方的支付与寄件地址信息确认，即NewOrderForm.jsp，接下来确认后是发货人地址与收货人地址信息确认（ConfirmOrder.jsp），或者点击修改地址即可通过ShippingFormServlet进行跳转到修改收件人地址界面，最后点击Comfirm之后即可生成最终订单（ViewOrder.jsp）。
>
> 第一个页面在购物车点击Proceed to Checkout之后利用NewOrderFormServlet进行跳转到地址信息显示界面。

NewOrderFormServlet:

```java
public class NewOrderFormServlet extends HttpServlet {
    private static final String NEW_ORDER = "/WEB-INF/jsp/order/NewOrderForm.jsp";
    private static final String SIGNONFORM = "/WEB-INF/jsp/account/SignonForm.jsp";
    private static final String ERROR = "/WEB-INF/jsp/common/Error.jsp";

    private Account account;
    private Order order;
    private Cart cart;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        account = (Account)session.getAttribute("account");
        cart = (Cart)session.getAttribute("cart");

        if (account == null){
            session.setAttribute("message", "You must sign on before attempting to check out.  Please sign on and try checking out again.");
            request.getRequestDispatcher(SIGNONFORM).forward(request, response);
        } else if(cart != null){
            order = new Order();
            order.initOrder(account, cart);
            session.setAttribute("order", order);
            request.getRequestDispatcher(NEW_ORDER).forward(request, response);
        }else{
            session.setAttribute("message", "An order could not be created because a cart could not be found.");
            request.getRequestDispatcher(ERROR).forward(request, response);
        }
    }
}
```

###### 3.5新增订单——地址信息即修改收件人地址

> 在生成订单的第一个界面时有个更改地址的选择框，选择之后点击Continue即可利用ShippingFormServlet跳转到更改收件人地址的界面ShippingForm.jsp,接下里可以进行收货人地址信息更改，再利用ShippingAdressServlet进行数据更新存入数据库接下来使用。

ShippingFormServlet:

```java
public class ShippingFormServlet extends HttpServlet {
    private static final String SHIPPINGFORM = "/WEB-INF/jsp/order/ShippingForm.jsp";
    private static final String SIGNONFORM = "/WEB-INF/jsp/account/SignonForm.jsp";

    private Account account;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        account = (Account)session.getAttribute("account");

        if (account == null){
            request.getRequestDispatcher(SIGNONFORM).forward(request, response);
        } else{
            request.getRequestDispatcher(SHIPPINGFORM).forward(request, response);
        }
    }
}
```

ShippingAdressServlet

```java
public class ShippingAddressServlet extends HttpServlet {
    private static final String CONFIRM_ORDER_FORM = "/WEB-INF/jsp/order/ConfirmOrder.jsp";

    private Order order;
    private String shipToFirstName;
    private String shipToLastName;
    private String shipAddress1;
    private String shipAddress2;
    private String shipCity;
    private String shipState;
    private String shipZip;
    private String shipCountry;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        shipToFirstName = request.getParameter("shipToFirstName");
        shipToLastName = request.getParameter("shipToLastName");
        shipAddress1 = request.getParameter("shipAddress1");
        shipAddress2 = request.getParameter("shipAddress2");
        shipCity = request.getParameter("shipCity");
        shipState = request.getParameter("shipState");
        shipZip = request.getParameter("shipZip");
        shipCountry = request.getParameter("shipCountry");

        HttpSession session = request.getSession();
        order = (Order)session.getAttribute("order");

        order.setShipToFirstName(shipToFirstName);
        order.setShipToLastName(shipToLastName);
        order.setShipAddress1(shipAddress1);
        order.setShipAddress2(shipAddress2);
        order.setShipCity(shipCity);
        order.setShipState(shipState);
        order.setShipZip(shipZip);
        order.setCourier(shipCountry);

        session.setAttribute("order", order);

        Account account = (Account)session.getAttribute("account");

        request.getRequestDispatcher(CONFIRM_ORDER_FORM).forward(request, response);
    }
}
```

###### 3.6新增订单——确定订单信息

> 在第一个地址页面后点击Continue进入第二个地址查看界面（ConfirmOrderFormServlet），确认订单信息的正误。

```java
public class ConfirmOrderFormServlet extends HttpServlet {
    private static final String CONFIRM_ORDER_FORM = "/WEB-INF/jsp/order/ConfirmOrder.jsp";
    private static final String SHIPPINGFORM = "/WEB-INF/jsp/order/ShippingForm.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        shippingAddressRequired = request.getParameter("shippingAddressRequired");
        order = new Order();

        HttpSession session = request.getSession();
        order = (Order)session.getAttribute("order");
        Account account = (Account)session.getAttribute("account");

        if (shippingAddressRequired == null){
request.getRequestDispatcher(CONFIRM_ORDER_FORM).forward(request, response);
        }
        else{
            shippingAddressRequired = null;
            request.getRequestDispatcher(SHIPPINGFORM).forward(request, response);
        }
    }
}
```

###### 3.7新增订单——生成最终订单

> 最终即可通过ViewOrderServlet生成最终的订单，包含地址信息、商品清单等信息.

ViewOrderServlet:

```java
public class ViewOrderServlet extends HttpServlet {
    private static final String VIEWORDER = "/WEB-INF/jsp/order/ViewOrder.jsp";
    private static final String ERROR = "/WEB-INF/jsp/common/Error.jsp";

    private Order order;
    private OrderService orderService;
    private Cart cart;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        order = (Order) session.getAttribute("order");
        cart = (Cart) session.getAttribute("cart");

        if (order != null) {
            orderService = new OrderService();
            orderService.insertOrder(order);
            session.setAttribute("order", order);
            //清空购物车
            cart = null;
            session.setAttribute("cart", cart);

            session.setAttribute("message", "Thank you, your order has been submitted.");
            request.getRequestDispatcher(VIEWORDER).forward(request, response);
        } else {
            session.setAttribute("message", "An error occurred processing your order (order was null).");
            request.getRequestDispatcher(ERROR).forward(request, response);
        }
    }
}
```

4. ##### 添加验证码功能：在用户注册和用户登录模块中添加验证码功能。

###### 4.1原理

先设置字符表即26个字母和各个数字，接下来利用字符表产生随机验证码，同时随机产生120个干扰点（点数适合即可，具体没有特别要求），产生图像，画背景，在不同高度输出验证码的不同字符，设置设置浏览器不要缓存此图片，然后创建内存图像并获得图形，将图像传到客户端，验证码功能即可实现随机显示。

###### 4.2核心代码

VerificationCode.java:

```java
 /*实现的核心代码*/
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("image/jpeg");
        HttpSession session=request.getSession();
        int width=60;
        int height=20;

        //设置浏览器不要缓存此图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //创建内存图像并获得图形上下文
        BufferedImage image=new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        Graphics g=image.getGraphics();

        /*
         * 产生随机验证码
         * 定义验证码的字符表
         */
        String chars="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] rands=new char[4];
        for(int i=0;i<4;i++){
            int rand=(int) (Math.random() *36);
            rands[i]=chars.charAt(rand);
        }

        /*
         * 产生图像
         * 画背景
         */
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);

        /*
         * 随机产生120个干扰点
         */

        for(int i=0;i<120;i++){
            int x=(int)(Math.random()*width);
            int y=(int)(Math.random()*height);
            int red=(int)(Math.random()*255);
            int green=(int)(Math.random()*255);
            int blue=(int)(Math.random()*255);
            g.setColor(new Color(red,green,blue));
            g.drawOval(x, y, 1, 0);
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font(null, Font.ITALIC|Font.BOLD,18));

        //在不同高度输出验证码的不同字符
        g.drawString(""+rands[0], 1, 17);
        g.drawString(""+rands[1], 16, 15);
        g.drawString(""+rands[2], 31, 18);
        g.drawString(""+rands[3], 46, 16);
        g.dispose();

        //将图像传到客户端
        ServletOutputStream sos=response.getOutputStream();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG", baos);
        byte[] buffer=baos.toByteArray();
        response.setContentLength(buffer.length);
        sos.write(buffer);
        baos.close();
        sos.close();

        session.setAttribute("checkcode", new String(rands));
    }

    public void init() throws ServletException {
        // Put your code here
    }
}
```

SigninForm.jsp:

```java
VerificationCode:<input type="text" name="vCode" size="5" maxlength="4"/>
<a href="signOn"><img border="0" src="verificationCode" name="checkcode"></a>
```

NewAccountForm.jsp:

```java
<tr>
   <td>VerificationCode:</td>
   <td>
      <input type="text" name="vCode" size="5" maxlength="4"/>
      <a href="newAccount"><img border="0" src="verificationCode" name="checkcode"></a>
   </td>
</tr>
```

SignOnServlet、NewAccountServlet：

```java
//获得输入的验证码值
String value1=request.getParameter("vCode");
/*获取图片的值*/
String value2=(String)session.getAttribute("checkcode");
Boolean isSame = false;
/*对比两个值（字母不区分大小写）*/
if(value2.equalsIgnoreCase(value1)){
    isSame = true;
}

if(isSame){
    ……
//具体登录代码
……
    request.getRequestDispatcher(MAIN).forward(request, response);
}else{
    ……
        LogService logService = new LogService();
        String logInfo = logService.logInfo(" ") + strBackUrl + " 注册账号，验证码错误";
        logService.insertLogInfo(account1.getUsername(), logInfo);
    }
    //验证码输入错误后刷新界面，重新生成验证码
    request.getRequestDispatcher(NEWACCOUNTFORM).forward(request, response);
}
```

5. ##### 日志功能：数据库中添加日志信息表，给项目添加日志功能，用户登录后记录用户行为，比如浏览了哪些商品、将商品添加进购物车、生成订单等。

   ###### 5.1所要记录的内容

用户账号、跳转时间、当前操作页面的网址、具体操作页面的名字、操作的商品信息、设计者自定义的具体操作说明。

###### 		5.2记录实现方法

利用Log.java和HTTPServletRequest的各种方法获取当前页面跳转时的用户账号、跳转时间、当前操作页面的网址、具体操作页面的名字、操作的商品信息，再加上自定义的具体操作说明，传输给LogService的相关方法即可将其插入到数据库中记录下来。

在每次页面跳转之前先获取当前对话中用户是否已登录，如果账号不为空，那么进行获取日志记录操作，并插入到数据库中，之后正常跳转。

如果用户未登录则不记录该操作

###### 		5.3核心代码

Log.java

```java
public class Log {
    public final static int OUT_CONSOLE=1;
    public final static int OUT_FILE=2;
    public final static int OUT_BOTH=3;
    public final static int DEBUG_LEVEL=0;
    public final static int LOG_LEVEL=3;
    public final static int INFO_LEVEL=6;
    public final static int WARN_LEVEL=9;
    public final static int ERROR_LEVEL=12;
    public final static int FATAL_LEVEL=15;
    protected static int level=DEBUG_LEVEL;// 输出的级别开关，高于指定级别的输出
    protected static int out=OUT_CONSOLE; //输出模式，输出到控制台，文件或者都输出
    public static String logdir="log";    // 保存日志文件的目录
    protected static BufferedWriter bw =null;
    protected static String currentDate = "";
    protected static Date data=null;
    protected static StringBuilder bb=new StringBuilder();
    protected static String marsk=" [ERROE] ";
    /**
     * 日志函数，不受 level 限制，始终打印，最高级别
     * @param s
     */
    synchronized public static String log(Object ...s) {
        bb.delete(0, bb.length());
        bb.append(time());
        bb.append(marsk);
        // 获取调用的位置
        //StackTraceElement[] stack = new Exception().getStackTrace();
        //bb.append(stack[2].getClassName().replaceAll("\\$\n.$", ""));
        bb.append("  ");
        /*
        bb.append("::");
        bb.append(stack[2].getMethodName());
        bb.append("()第[");
        bb.append(stack[2].getLineNumber());
        bb.append("]行： ");
        */
        for(Object str : s) {
            bb.append(str);
            bb.append(" ");
        }
        //out(bb.toString());
        return bb.toString();
    }

    public static String logInfomation(Object ...s){
        marsk = " ";
        return log(s);
    }

    /*
    public static void debug(Object ...s) {
        if(level<=DEBUG_LEVEL) {
            marsk=" [DEBUG] ";
            log(s);
        }

    }
    public static void info(Object ...s) {
        if(level<=INFO_LEVEL){
            marsk=" [INFO ] ";
            log(s);
        }
    }
    public static void warn(Object ...s) {
        if(level<=WARN_LEVEL){
            marsk=" [WARN ] ";
            log(s);
        }
    }
    public static void error(Object ...s) {
        if(level<=ERROR_LEVEL){
            marsk=" [ERROR] ";
            log(s);
        }
    }
    public static void fatal(Object ...s) {
        if(level<=FATAL_LEVEL){
            //marsk=" [FATAL] ";
            marsk = "  ";
            log(s);
        }
    }
    protected static void out(String s) {
        if(out==OUT_BOTH) {
            System.out.println(s);
            tofile(s);
        }else if(out==OUT_FILE) {
            tofile(s);
        }else if(out==OUT_CONSOLE) {
            System.out.println(s);
        }
    }
    protected static void tofile(String s) {
        getWriter();
        try {
            bw.newLine();
            bw.write(s);
            bw.flush();
        }catch(Exception e) {

        }
    }
    */


    protected static BufferedWriter getWriter() {
        if(currentDate.equals(new SimpleDateFormat("dd").format(data))) {
            return bw;
        }
        File fi=new File(logdir
                +File.separator+new SimpleDateFormat("yyyy年").format(data)
                +File.separator+new SimpleDateFormat("MM月").format(data)
                +File.separator+new SimpleDateFormat("dd").format(data)+"日.log");
        try {
            if(!fi.exists()) {
                if(!fi.getParentFile().exists())
                    fi.getParentFile().mkdirs();
                fi.createNewFile();
            }
            FileOutputStream fiou = new FileOutputStream(fi,true);
            OutputStreamWriter opw = new OutputStreamWriter(fiou, "UTF-8");
            bw= new BufferedWriter(opw);
        }catch(Exception e) {

        }
        return bw;
    }
    protected static String time() {
        data = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data);
    }

    /**
     * 初始化
     * @param out_put_level 日志输出等级,只输出较高等级的 debug < info < warn < error < fatal
     * @param out2where 输出到哪里 OUT_CONSOLE仅仅控制台输出,OUT_FILE仅仅输出到文件,OUT_BOTH均输出
     * @param log_dir 日志保存到哪里
     */
    public static void init(int out_put_level,int out2where,String log_dir) {
        level = out_put_level;
        out = out2where;
        logdir = log_dir;
    }

    public static void main(String[] args) {
        //Log.debug("这是一个错误","可变长参数","无需拼接字符串");
    }
}
```

LogService:

```java
public class LogService {
    Log log;
    private LogDAO logDAO;

    public LogService(){
        log = new Log();
        logDAO = new LogDAOImpl();
    }

    public String logInfo(Object ...s){
        return log.logInfomation(s);
    }

    public void insertLogInfo(String username, String logInfo){
        logDAO.insertLog(username, logInfo);
    }
}
```











































