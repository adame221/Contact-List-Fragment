package com.ElyAdam.AELYLab13_1;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Owner on 11/28/2015.
 */
public class ContactListFragment extends Fragment implements Serializable, AppInfo{

    private TextView mContactsAddedText;
    private TextView mContactsText;
    private ListView mNewListView;
    private ActionBar myActionBar;
    ObjectArrayAdapter mAdapter;

    private String strContactsAddedText = "";
    private String strContactsText = "";
    private ArrayList<Contact> mContacts;
    private Contact mUpdatedContact;
    private Contact mAddedContact;
    OnAddListItemSelected mCallback;
    private int mClickedInt;
    private int mPlaceholder;
    private Bundle mArgs;

    public interface OnAddListItemSelected {
        /**
         * This method is used to pass the array of contacts when
         * the user clicks on the add contact button. This method
         * will then also shoot off the contact fragment
         * @param mContacts
         */
        void onAddContactWithArray(ArrayList<Contact> mContacts);

        /**
         * This method send the item of the clicked position, the array
         * of contacts, and the position to the contact fragment, when the
         * user clicks on a lis
         * @param item
         * @param mContacts
         * @param position
         */
        void onClickContact(Contact item, ArrayList<Contact> mContacts, int position);
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

        outState.putString(getResources().getString(R.string.contacts_added), mContactsText
                .getText().toString());
        outState.putSerializable(getResources().getString(R.string.contact_array), mContacts);


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

        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        setRetainInstance(true);

        mArgs = setArguments();

        if(mContacts == null) {
            mContacts = new ArrayList<>();
        }

        ActionBar myActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if(mArgs != null) {
            setHasOptionsMenu(true);
            myActionBar.setTitle("Lab 13.1");
            myActionBar.setSubtitle("Contact List");
            myActionBar.setDisplayHomeAsUpEnabled(false);
        }

        mNewListView = (ListView) view.findViewById(R.id.newList);
        mContactsAddedText = (TextView) view.findViewById(R.id.contactsAddedText);
        mContactsText = (TextView) view.findViewById(R.id.contactsText);

        setNoContactsToDisplay();

        strContactsAddedText = mContactsAddedText.getText().toString();
        strContactsText = mContactsText.getText().toString();

        mContacts = addNewContact(savedInstanceState, mAddedContact, mContacts);

        mNewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                final Contact item = (Contact) mNewListView.getAdapter().getItem(position);
                mCallback.onClickContact(item, mContacts, position);
            }
        });

        mContacts = addUpdatedContact(savedInstanceState, mUpdatedContact, mContacts, mClickedInt);
        setTextFields(mContacts);

        if(savedInstanceState != null){
            mContacts = (ArrayList<Contact>)savedInstanceState.getSerializable(getResources()
                    .getString(R.string.contact_array));
            mContactsText.setText(savedInstanceState.getString(getResources().getString(R.string
                    .contacts_added)));

            mAdapter = new ObjectArrayAdapter(this.getActivity(), R.layout.detail_line, mContacts);
            mNewListView.setAdapter(mAdapter);
        }

        return view;
    }

    /**
     * This class receives the arguments from the main activity and sets
     * the information to there corresponding fields.
     */
    private Bundle setArguments(){
        Bundle args = getArguments();
        if(args != null) {
            mPlaceholder = args.getInt(PLACE_HOLDER);
            mAddedContact = (Contact) args.getSerializable(EXTRA_CONTACT);
            mContacts = (ArrayList<Contact>) args.getSerializable(PASSED_CONTACT_ARRAY_WITH_CONTACTS);
            mUpdatedContact = (Contact) args.getSerializable(UPDATED_CONTACT);
            mClickedInt = args.getInt(CLICKED_INT);
        }
        return args;
    }

    /**
     * This method sets the text for no contacts to display
     */
    private void setNoContactsToDisplay() {
        if (mContacts.size() == 0) {
            mContactsText.setText("No contacts to display");
        }
    }

    /**
     * This method adds a new contact to to the array list
     */
    public ArrayList<Contact> addNewContact(Bundle savedInstanceState, Contact addedContactTablet, ArrayList<Contact>
            mContactsTablet) {
            if (addedContactTablet != null) {
                mContactsText.setText(null);

                if (savedInstanceState == null) {
                    mContactsTablet.add(addedContactTablet);
                }

                mContacts = mContactsTablet;

                mContactsAddedText.setText(getResources().getString(R.string.contacts_added_label) +
                        mContactsTablet.size());

                mAdapter = new ObjectArrayAdapter(this.getActivity(), R.layout.detail_line, mContactsTablet);
                mNewListView.setAdapter(mAdapter);
        }
        return mContactsTablet;
    }

    /**
     * This method adds the updated contact to the array list of contacts
     * @param savedInstanceState Bundle
     */
    public ArrayList<Contact> addUpdatedContact(Bundle savedInstanceState, Contact mUpdatedContactTablet,
                                  ArrayList<Contact> mContactsTablet, int mClickedIntTablet) {
            if (mUpdatedContactTablet != null && savedInstanceState == null) {
                mContactsTablet.remove(mClickedIntTablet);
                mContactsTablet.add(mUpdatedContactTablet);
                Toast.makeText(getActivity(), getResources().getString(R.string.contact_updated),
                        Toast.LENGTH_SHORT).show();
                switch (mContactsTablet.size() - 1) {
                }
            }
            mContacts = mContactsTablet;
            mAdapter = new ObjectArrayAdapter(this.getActivity(), R.layout.detail_line, mContactsTablet);
            mNewListView.setAdapter(mAdapter);
        return mContactsTablet;
    }

    /**
     * This method sets the text field for this fragment
     */
    private void setTextFields(ArrayList<Contact> mContacts) {
        mContactsAddedText.setText(getResources().getString(R.string.contacts_added_label) +
                mContacts.size
                        ());

        mAdapter = new ObjectArrayAdapter(this.getActivity(), R.layout.detail_line, mContacts);
        mNewListView.setAdapter(mAdapter);
    }

    public class ObjectArrayAdapter extends ArrayAdapter<Contact> {

        //declare ArrayList of item
        private ArrayList<Contact> contacts;

        /**
         *  Override the constructor for ArrayAdapter
         *  The only variable we care about now ArrayList<PlatformVersion> objects
         *  it is the list of the objects we want to display
         *  @param context
         * @param resource
         * @param contacts
         */

        public ObjectArrayAdapter(Context context, int resource, ArrayList<Contact> contacts) {
            super(context, resource, contacts);
            this.contacts = contacts;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.detail_line, null);
            }

            Contact contactObject = contacts.get(position);

            if (contactObject != null) {
                TextView mLastName = (TextView) view.findViewById(R.id.lastName);
                TextView mFirstName = (TextView) view.findViewById(R.id.firstName);
                TextView mComma = (TextView) view.findViewById(R.id.comma);
                TextView mEmail = (TextView) view.findViewById(R.id.email);
                TextView mPhone = (TextView) view.findViewById(R.id.phone);

                if (mLastName != null) {
                    mLastName.setText(contactObject.getLastName());
                }
                if (mFirstName != null) {
                    mFirstName.setText(contactObject.getFirstName());
                }
                if (mEmail != null) {
                    mEmail.setText(contactObject.getEmail());
                }

                if (mPhone != null) {
                    mPhone.setText(contactObject.getPhoneNumber());
                }
            }

            return view;
        }
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
            mCallback = (OnAddListItemSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link Activity#onCreateOptionsMenu(Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if(mArgs != null) {
            menu.findItem(R.id.action_contact_info).setVisible(false);
        }
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
        switch (item.getItemId()){
           case R.id.action_single_pane_contact_info:
                mCallback.onAddContactWithArray(mContacts);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
