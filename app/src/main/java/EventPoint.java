import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gregory Chang on 3/30/2016.
 */
public class EventPoint implements Parcelable {

    int x;
    int y;
    String type = "";
    int time;

    public EventPoint (int x, int y, String type, int time) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.time = time;
    }

    private EventPoint (Parcel in) {
        x = in.readInt();
        y = in.readInt();
        type = in.readString();
        time = in.readInt();
    }


    public static final Creator<EventPoint> CREATOR = new Creator<EventPoint>() {
        @Override
        public EventPoint createFromParcel(Parcel in) {
            return new EventPoint(in);
        }

        @Override
        public EventPoint[] newArray(int size) {
            return new EventPoint[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeString(type);
        dest.writeInt(time);
    }
}
