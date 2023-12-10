package es.rafapuig.persistence.agenda.withfiles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.rafapuig.persistence.agenda.withfiles.databinding.ActivityRegistersBinding;
import es.rafapuig.persistence.agenda.withfiles.model.Person;

public class RegistersActivity extends AppCompatActivity {

    private List<Person> people = new ArrayList<>();
    private int currentPersonIndex = -1; // Index in the collection of the person to show

    ActivityRegistersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_registers);
        binding = ActivityRegistersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TODO: call a method to load People's data stored in the file
        loadPeopleData();

        // TODO: call a method to point to the first person in the collection
        initPeopleRegisterIndex();

        // TODO: Call a method to show the current Person in the UI
        showCurrentPersonData();
    }


    Person getPersonFromIndex(int index) {
        // TODO: return a reference to the person located in the provided index, if index is out of bounds return null
        try {
            return people.get(index);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }


    void addPerson(Person person) {
        // TODO: Add the element Person to de Collection
        //people = Arrays.copyOf(people, people.length + 1);
        //people[people.length - 1] = person;
        people.add(person);
    }


    void initPeopleRegisterIndex() {
        // TODO: Point to the first person in the collection
        if (!people.isEmpty()) currentPersonIndex = 0;
        else currentPersonIndex = -1;
    }


    void processRegisterData(String fullName, int age, boolean isWorking) {
        // TODO: Create a new Person with the method parameters and add to the data structure
        Person person = new Person(fullName, age, isWorking);
        addPerson(person);
    }

    private void loadPeopleData() {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        try {
            // TODO: read from file each register, get the fields that make up a Person object and process the data

            fis = getFileInputStream(MainActivity.FILENAME);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            //We read in an infinite loop because eventually the reading will throw an EOFException
            //when we have exhausted all the bytes of the input stream
            while(true) {
                //We read the 3 field that make up a register in the same order as the were saved
                //in the file, if not, we will misinterpret the byte info
                String fullName = dis.readUTF();
                int age = dis.readInt();
                boolean isWorking = dis.readBoolean();

                //Do something with the read data
                processRegisterData(fullName, age, isWorking);
            }

        } catch (EOFException e) //We have ended the reading of data in the file
        {
            try {
                // TODO: close all input streams!
                dis.close();
                bis.close();
                fis.close();

            } catch (IOException ioe)
            {
                Toast.makeText(this,"There was an error closing the streams!: " +
                        ioe.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        catch (FileNotFoundException fnfe)
        {
            // File not exists!
            Toast.makeText(this,"STILL NO DATA", Toast.LENGTH_LONG).show();
        }
        catch (IOException ioe)
        {
            Toast.makeText(this,"Error reading the file!: " +
                    ioe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    private FileInputStream getFileInputStream(String filename) throws FileNotFoundException
    {
        if (MainActivity.isWriteableSD) // There is an SD
            return new FileInputStream(MainActivity.getSDCardFile(filename));
        else
            return this.openFileInput(filename); // If there's no SD, use internal storage
    }


    public void onPreviousButtonClickCallback(View view) {
        if (currentPersonIndex > 0) {
            // TODO: Do currentPersonIndex points to previous position in the array and show data
            currentPersonIndex--;
            showCurrentPersonData();
        } else {
            Toast.makeText(this, "There's no previous register", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNextButtonClickCallback(View view) {
        if (currentPersonIndex < people.size() - 1) {
            // TODO: Do currentPersonIndex points to next position in the array and show data
            currentPersonIndex++;
            showCurrentPersonData();
        } else {
            Toast.makeText(this, "There's no next register", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackButtonClickCallback(View view) {
        this.finish();
    }

    void showCurrentPersonData() {
        // TODO: Show in the UI data form the current array index pointer Person by currentPersonIndex
        Person person = getPersonFromIndex(currentPersonIndex);

        if (person != null) {
            binding.fullNameRegisterTextView.setText(person.getFullName());
            binding.ageRegisterTextView.setText(Integer.toString(person.getAge()));
            binding.isWorkingRegisterTextView.setText(person.isWorking() ? "YES" : "NO");
        }
    }
}