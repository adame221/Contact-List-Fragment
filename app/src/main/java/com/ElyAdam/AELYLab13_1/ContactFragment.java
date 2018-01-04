package com.ElyAdam.AELYLab13_1;

import android.R.id;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ElyAdam.AELYLab13_1.R.layout;
import com.ElyAdam.AELYLab13_1.R.string;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Owner on 11/28/2015.
 */
public class ContactFragment extends Fragment implements Serializable, AppInfo{

    private TextView mFirstNameLabel;
    private EditText mEditFirstName;
    private TextView mLastNameLabel;
    private EditText mEditLastName;
    private TextView mEmailLabel;
    private EditText mEditEmail;
    private TextView mPhoneLabel;
    private EditText mEditPhone;
    private Button mAddButton;
    private String strEditFirstName;
    private String strEditLastName;
    private String strEmail;
    private String strPhone;
    private String mUpdatedFirstName;
    private String mUpdatedLastName;
    private String mUpdatedEmail;
    private String mUpdatedPhone;
    Contact newContact;
    ContactFragment.OnAddContact mCallback;
    private ArrayList<Contact> mArrayListOfContacts;
    private int mClickedInt;
    private Bundle mArgs;
    private String strAddButton;

    public interface OnAddContact {
        /**
         * This method sends the information for a contact, and the array list of contacts
         * to the contact list fragment, when the user clicks the ADD button.
         * @param contact
         * @param mArrayListWithContacts
         */
        void onClickAddNewContactWithContacts(Contact contact, ArrayList<Contact> mArrayListWithContacts);

        /**
         * This method sends the updated contact information, the array of contacts and the clicked
         * position from the contact list fragment class to the contact list fragment class, when
         * the user clicks the UPDATE button.
         * @param updatedContact
         * @param mArrayListWithContacts
         * @param mClickedInt
         */
        void onClickUpdateContact(Contact updatedContact, ArrayList<Contact> mArrayListWithContacts, int mClickedInt);
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getResources().getString(string.edit_first_name), strEditFirstName);
        outState.putString(getResources().getString(string.edit_last_name), strEditLastName);
        outState.putString(getResources().getString(string.edit_email), strEmail);
        outState.putString(getResources().getString(string.edit_phone), strPhone);
        outState.putSerializable(getResources().getString(string.array_list_with_contacts), mArrayListOfContacts);
        outState.putString(getResources().getString(string.string_add_button), strAddButton);
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            mEditFirstName.setText(savedInstanceState.getString(getResources().getString(string.edit_first_name)));

            mEditLastName.setText(savedInstanceState.getString(getResources().getString(string
                    .edit_last_name)));

            mEditEmail.setText(savedInstanceState.getString(getResources().getString(string.edit_email)));

            mEditPhone.setText(savedInstanceState.getString(getResources().getString(string.edit_phone)));

            mArrayListOfContacts = (ArrayList<Contact>)savedInstanceState.getSerializable(getResources()
                    .getString(string.array_list_with_contacts));
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(layout.fragment_contact, container, false);

        mArgs = setArguments();

        if(mArrayListOfContacts == null) {
            mArrayListOfContacts = new ArrayList<>();
        }
        ActionBar contactActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(mArgs != null) {
            contactActionBar.setTitle("Contact Info");
            setHasOptionsMenu(true);
            contactActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mFirstNameLabel = (TextView) view.findViewById(R.id.firstNameLabel);
        mEditFirstName = (EditText) view.findViewById(R.id.editFirstName);
        mLastNameLabel = (TextView) view.findViewById(R.id.lastNameLabel);
        mEditLastName = (EditText) view.findViewById(R.id.editLastName);
        mEmailLabel = (TextView) view.findViewById(R.id.emailLabel);
        mEditEmail = (EditText) view.findViewById(R.id.editEmail);
        mPhoneLabel = (TextView) view.findViewById(R.id.phoneLabel);
        mEditPhone = (EditText) view.findViewById(R.id.editPhone);
        mAddButton = (Button) view.findViewById(R.id.addButton);

        mArrayListOfContacts = setFieldsWithUpdatedContact(savedInstanceState, newContact,
                mArrayListOfContacts,
                mClickedInt);

        strAddButton = mAddButton.getText().toString();

        /**
         * This method adds the entered information to the main activity
         */
        mAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mArrayListOfContacts = setNewContact(mArrayListOfContacts);
                mArrayListOfContacts = setUpdatedContact(mArrayListOfContacts, mClickedInt);
            }
        });

        if(savedInstanceState != null){

            mArrayListOfContacts = (ArrayList<Contact>)savedInstanceState.getSerializable(getResources()
                    .getString(string.array_list_with_contacts));

            strAddButton = savedInstanceState.getString(getResources()
                    .getString(string.string_add_button));

            mAddButton.setText(strAddButton);
        }
        return view;
    }

    /**
     * This method sets the arguments for this class
     */
    private Bundle setArguments() {
        Bundle args = getArguments();
        if(args != null) {
            mArrayListOfContacts = (ArrayList<Contact>) args.getSerializable(AppInfo.CONTACT_ARRAY_WITH_CONTACTS);
            newContact = (Contact) args.getSerializable(AppInfo.CONTACT_ITEM);
            mClickedInt = args.getInt(AppInfo.CLICKED_INT);
        }
        return args;
    }

    /**
     * This method sets the text field with the selected contact
     */
    public ArrayList<Contact> setFieldsWithUpdatedContact(Bundle savedInstanceState, Contact
            updatedContact, ArrayList<Contact> mContacts, int position) {
        if (updatedContact != null) {
            mEditFirstName.setText(updatedContact.getFirstName());
            mEditLastName.setText(updatedContact.getLastName());

            if (updatedContact.getEmail() != null) {
                mEditEmail.setText(updatedContact.getEmail());
            }

            if(updatedContact.getPhoneNumber() != null) {
                mEditPhone.setText(updatedContact.getPhoneNumber());
            }
            mAddButton.setText("Update");
             strAddButton = mAddButton.getText().toString();
        }
        mArrayListOfContacts = mContacts;
        return mContacts;
    }

    /**
     * This method sets the updated contact
     */
    public ArrayList<Contact> setUpdatedContact(ArrayList<Contact> mArrayListOfContact, int mClickedIntTablet) {
        if (mAddButton.getText().equals("Update")) {
            boolean valid = true;
            mUpdatedFirstName = mEditFirstName.getText().toString();
            mUpdatedLastName = mEditLastName.getText().toString();
            mUpdatedEmail = mEditEmail.getText().toString();
            mUpdatedPhone = mEditPhone.getText().toString();

            if (mUpdatedFirstName.equals("")) {
                mEditFirstName.setError(getResources().getString(string.first_name_required));
                valid = false;
            }

            if (mUpdatedLastName.equals("")) {
                mEditLastName.setError(getResources().getString(string.last_name_required));
                valid = false;
            }

            if (valid) {
                Contact mUpdatedContact = new Contact(mUpdatedFirstName, mUpdatedLastName, mUpdatedEmail, mUpdatedPhone);
                mCallback.onClickUpdateContact(mUpdatedContact, mArrayListOfContact, mClickedIntTablet);
            }
        }
        mArrayListOfContacts = mArrayListOfContact;
        return mArrayListOfContact;
    }

    /**
     * This method sets the new contact
     */
    public ArrayList<Contact> setNewContact(ArrayList<Contact> mArrayListOfContact) {
        if(mAddButton.getText().equals("Add")) {
            boolean valid = true;
            strEditFirstName = mEditFirstName.getText().toString();
            strEditLastName = mEditLastName.getText().toString();
            strEmail = mEditEmail.getText().toString();
            strPhone = mEditPhone.getText().toString();

            if (strEditFirstName.equals("")) {
                mEditFirstName.setError(getResources().getString(string.first_name_required));
                valid = false;
            }

            if (strEditLastName.equals("")) {
                mEditLastName.setError(getResources().getString(string.last_name_required));
                valid = false;
            }
            if (valid) {
                Contact contact = new Contact(strEditFirstName, strEditLastName, strEmail, strPhone);
                mCallback.onClickAddNewContactWithContacts(contact, mArrayListOfContact);
            }
            strAddButton = mAddButton.getText().toString();
        }
        mArrayListOfContacts = mArrayListOfContact;
        return mArrayListOfContact;
    }

    /**
     * This method clears the text in this fragment when the activity is in two-pane.
     * @param mContacts
     */
    public ArrayList<Contact> clearText(ArrayList<Contact> mContacts) {
       if(mArrayListOfContacts == null) {
           mArrayListOfContacts = mContacts;
       }
        mEditFirstName.setText(null);
        mEditLastName.setText(null);
        mEditEmail.setText(null);
        mEditPhone.setText(null);
        mAddButton.setText("Add");
        return mContacts;
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (ContactFragment.OnAddContact) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

      menu.findItem(R.id.action_single_pane_contact_info).setVisible(false);
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
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.home:
                mCallback.onClickAddNewContactWithContacts(null, mArrayListOfContacts);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
