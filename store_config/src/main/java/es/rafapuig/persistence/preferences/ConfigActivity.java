package es.rafapuig.persistence.preferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConfigActivity extends AppCompatActivity {

    private int[] colors = {Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.CYAN};
    private String[] sColors = {"White", "Red", "Green", "Blue", "Cyan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        fillColorsSpinner();

        Intent callerIntent = this.getIntent();
        int color = callerIntent.getIntExtra("color", MainActivity.DEFAULT_BACKCOLOR);
        float transparency = callerIntent.getFloatExtra("transparency", MainActivity.DEFAULT_OPACITY);

        setTransparencyCheckbox(transparency);
        setSelectedColor(color);
    }

    void fillColorsSpinner() {
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sColors);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }


    private void setSelectedColor(int color) {
        Spinner spinner = this.findViewById(R.id.spinner);

        int index = indexOf(IntStream.of(colors).boxed().toArray(), color);

        if (index != -1) {
            spinner.setSelection(index);
        }
    }

    /*private <T> int indexOf(T[] array, T item) {
        int index;
        for(index = 0; index< array.length; index++) {
            if(array[index].equals(item)) break;
        }
        /*int index = 0;
        while (index < array.length && !array[index].equals(item)) {
            index++;
        };
        return index < array.length ? index : -1;
    }*/

    private <T> int indexOf(T[] array, T item) {

        int position = IntStream.range(0,array.length)
                .filter(index -> array[index].equals(item))
                .findAny()
                .orElse(-1);

        return position;
    }



    private void setTransparencyCheckbox(float transparency) {
        CheckBox transparencyCheckBox = this.findViewById(R.id.checkBox);
        transparencyCheckBox.setChecked(transparency < 1.0f);
    }

    private float getTransparency() {
        CheckBox transparencyCheckBox = this.findViewById(R.id.checkBox);
        return transparencyCheckBox.isChecked() ? 0.5f : 1.0f;
    }

    private int getSelectedColor() {
        Spinner spinner = this.findViewById(R.id.spinner);
        int index = spinner.getSelectedItemPosition();
        return colors[index];
    }

    public void onClickApplyCallback(View view) {
        Intent result = new Intent();
        result.putExtra("color", this.getSelectedColor());
        result.putExtra("transparency", this.getTransparency());
        this.setResult(RESULT_OK, result);
        this.finish();
    }
}