package io.realm.typed;

import android.test.AndroidTestCase;

import io.realm.typed.entities.User;


public class RealmTest extends AndroidTestCase {

    public void testRealm() {

        // Init
        RealmList<User> users = Realms.list(this.getContext(), User.class);
        // Notice that RealmList implements List, which means that it can be used in a lot of existing code
        // We could also implement RealmMap, RealmSet, etc.


        try {
            users.beginWrite();

            // Insert
            for (int i = 0; i < 120; i++) {

                User user = users.create();

                user.setId(i);
                user.setName("Rasmus");
                user.setEmail("ra@realm.io");

            //    users.add(user);

                user.setId(10);

            }

            users.commit();

        } catch(Throwable t) {
            t.printStackTrace();
            users.rollback();
        }

        // Get
        User user1 = users.get(100);
        assertEquals("Rasmus", user1.getName());

        try {
            users.beginWrite();
            user1.setName("TestName");
            users.commit();
        } catch(Throwable t) {
            users.rollback();
        }


        assertEquals("TestName", user1.getName());

        assertEquals(120, users.size());

        // Iterable
        for(User user : users) {
            System.out.println(user.getId());
        }

        try {
            users.beginWrite();
            user1.setId(100);
            users.commit();
        } catch(Throwable t) {
            users.rollback();
        }

        // Query
        RealmList<User> results = users.where().equalTo("id", 10).findAll();

        assertEquals(119, results.size());
        assertEquals(10, results.get(0).getId());

    }

}
