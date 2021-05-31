package model;

import java.io.Serializable;

public class Stadium implements Serializable {
	private String stadiumName;
	private String location;
	private int numOfSeatsInStadium;

	public Stadium(String stadiumName, String stadiumLocation, int intNumOfSeatsInStadium) {
		setStadiumName(stadiumName);
		setLocation(stadiumLocation);
		setNumOfSeatsInStadium(intNumOfSeatsInStadium);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stadium other = (Stadium) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (numOfSeatsInStadium != other.numOfSeatsInStadium)
			return false;
		if (stadiumName == null) {
			if (other.stadiumName != null)
				return false;
		} else if (!stadiumName.equals(other.stadiumName))
			return false;
		return true;
	}

	public String getStadiumName() {
		return stadiumName;
	}

	private void setStadiumName(String stadiumName) {
		this.stadiumName = stadiumName;
	}

	public String getLocation() {
		return location;
	}

	private void setLocation(String location) {
		this.location = location;
	}

	public int getNumOfSeatsInStadium() {
		return numOfSeatsInStadium;
	}

	private void setNumOfSeatsInStadium(int numOfSeatsInStadium) {
		this.numOfSeatsInStadium = numOfSeatsInStadium;
	}

	@Override
	public String toString() {
		return "Stadium name: " + stadiumName + "\nLocation: " + location + "\nNumber of seats in Stadium: "
				+ numOfSeatsInStadium + "\n";
	}

}
