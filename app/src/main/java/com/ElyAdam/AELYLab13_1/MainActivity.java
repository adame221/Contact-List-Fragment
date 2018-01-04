package com.ElyAdam.AELYLab13_1;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable,
        ContactListFragment.OnAddListItemSelected, ContactFragment.OnAddContact{

    private Toolbar mToolbar;
    ActionBar myActionBar;
    private ArrayList<Contact> mAddedContacts;
    private boolean mDualPane = true;
    private int mPlaceholder = 1;

    /**
     * Save all appropriate fragment state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getResources().getString(R.string.array_added_contacts), mAddedContacts);
    }

    /**
     * Perform initialization of all fragments and loaders.
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(mToolbar);

        myActionBar = getSupportActionBar();

        myActionBar.setTitle(R.string.app_name);
        myActionBar.setSubtitle("Contact List");

        if (findViewById(R.id.fragmentContainer) != null) {
            mDualPane = false;
                if (savedInstanceState == null) {

                ContactListFragment firstFragment = new ContactListFragment();
                Bundle contactBundle = new Bundle();
                contactBundle.putInt(ContactFragment.PLACE_HOLDER, mPlaceholder);
                firstFragment.setArguments(contactBundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, firstFragment).commit();
            }
        } else {
            mDualPane = true;
        }
    }

    /**
     * This method is used to pass the array of contacts when
     * the user clicks on the add contact button. This method
     * will then also shoot off the contact fragment
     * @param mContacts
     */
    @Override
    public void onAddContactWithArray(ArrayList<Contact> mContacts) {
        ContactFragment contactFrag = (ContactFragment)
                getSupportFragmentManager().findFragmentById(R.id.contact_fragment);

        if (contactFrag != null) {
            contactFrag.setNewContact(mContacts);
        } else {
            ContactFragment contact = new ContactFragment();
            Bundle contactBundle = new Bundle();
            contactBundle.putSerializable(ContactFragment.CONTACT_ARRAY_WITH_CONTACTS, mContacts);
            contact.setArguments(contactBundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, contact);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    /**
     * This method sends the information for a contact, and the array list of contacts
     * to the contact list fragment, when the user clicks the ADD button.
     * @param contact
     * @param mArrayListWithContacts
     */
    @Override
    public void onClickAddNewContactWithContacts(Contact contact, ArrayList<Contact> mArrayListWithContacts) {
        ContactListFragment contactListFrag = (ContactListFragment)
                getSupportFragmentManager().findFragmentById(R.id.contact_list_fragment);

        if (contactListFrag != null) {
            contactListFrag.addNewContact(null, contact, mArrayListWithContacts);
        } else {
            ContactListFragment contactList = new ContactListFragment();
            Bundle contactListBundle = new Bundle();
            contactListBundle.putSerializable(ContactListFragment.PASSED_CONTACT_ARRAY_WITH_CONTACTS, mArrayListWithContacts);
            contactListBundle.putSerializable(ContactListFragment.EXTRA_CONTACT, contact);
            contactList.setArguments(contactListBundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, contactList);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * This method send the item of the clicked position, the array
     * of contacts, and the position to the contact fragment, when the
     * user clicks on a list item.
     * @param item
     * @param mContacts
     * @param position
     */
    @Override
    public void onClickContact(Contact item, ArrayList<Contact> mContacts, int position) {
        ContactFragment contactFrag = (ContactFragment)
                getSupportFragmentManager().findFragmentById(R.id.contact_fragment);

        if (contactFrag != null) {
            contactFrag.setFieldsWithUpdatedContact(null, item, mContacts, position);
        } else {
            ContactFragment contact = new ContactFragment();
            Bundle contactBundle = new Bundle();
            contactBundle.putSerializable(ContactListFragment.CONTACT_ITEM, item);
            contactBundle.putSerializable(ContactFragment.CONTACT_ARRAY_WITH_CONTACTS, mContacts);
            contactBundle.putInt(ContactFragment.CLICKED_INT, position);
            contact.setArguments(contactBundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, contact);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * This method sends the updated contact information, the array of contacts and the clicked
     * position from the contact list fragment class to the contact list fragment class, when
     * the user clicks the UPDATE button.
     * @param updatedContact
     * @param mArrayListWithContacts
     * @param mClickedInt
     */
    @Override
    public void onClickUpdateContact(Contact updatedContact, ArrayList<Contact> mArrayListWithContacts, int mClickedInt) {
        ContactListFragment contactListFrag = (ContactListFragment)
                getSupportFragmentManager().findFragmentById(R.id.contact_list_fragment);

        if (contactListFrag != null) {
            contactListFrag.addUpdatedContact(null, updatedContact, mArrayListWithContacts, mClickedInt);
        } else {
            ContactListFragment contactList = new ContactListFragment();
            Bundle contactListBundle = new Bundle();
            contactListBundle.putSerializable(ContactListFragment.PASSED_CONTACT_ARRAY_WITH_CONTACTS, mArrayListWithContacts);
            contactListBundle.putSerializable(ContactListFragment.UPDATED_CONTACT, updatedContact);
            contactListBundle.putInt(ContactListFragment.CLICKED_INT, mClickedInt);
            contactList.setArguments(contactListBundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, contactList);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        mAddedContacts = new ArrayList<>();

        if(!mDualPane) {
            menu.findItem(R.id.action_contact_info).setVisible(false);
        } else {
            menu.findItem(R.id.action_single_pane_contact_info).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p/>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ContactFragment contactFrag = (ContactFragment)
                getSupportFragmentManager().findFragmentById(R.id.contact_fragment);

        switch (item.getItemId()) {
            case R.id.action_contact_info:
                contactFrag.clearText(mAddedContacts);
                return true;
            case R.id.action_help:
                Toast.makeText(getApplicationContext(), "Help is coming soon", Toast
                        .LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
