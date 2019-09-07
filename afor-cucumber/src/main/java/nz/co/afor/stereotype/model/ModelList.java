package nz.co.afor.stereotype.model;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.Before;

import java.util.ArrayList;

public class ModelList<T> extends ArrayList<T> implements Model<T> {
}
