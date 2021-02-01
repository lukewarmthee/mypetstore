package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.CartItem;

public interface CartDAO {
    void insertCartItem(String workingItemId, Account account);

    void removeCartItem(String workingItemId, Account account);

    void updateCartItem(String workingItemId, Account account);


}
