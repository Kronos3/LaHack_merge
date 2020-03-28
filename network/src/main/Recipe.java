package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Recipe extends Requestable {
    /*
    name = models.CharField(max_length=128)
	id = models.CharField(max_length=128)
	minutes = models.IntegerField()
	contributor_id = models.CharField(max_length=128)
	submitted = models.DateField()

	tags = models.ManyToManyField(Tag)

	ingredients = models.ManyToManyField(Ingredient)
	image_cache = models.URLField()

     */

    private String name;
    private String id;
    private int minutes;
    private String contributor_id;
    private Date submitted;

    private ArrayList<Ingredient> ingredients;
    private ArrayList<Tag> tags;

    private String imageUrl;

    public Recipe(JSONObject object) {
        super(object);

        this.name = (String)this.get_obj().get("name");
        this.id = (String)this.get_obj().get("id");

        this.minutes = (int)Integer.parseInt((String)this.get_obj().get("minutes"));
        this.contributor_id = (String)this.get_obj().get("contributor_id");
        this.imageUrl = (String)this.get_obj().get("imageUrl");
        this.submitted = new Date(Long.parseLong((String)this.get_obj().get("submitted")));

        JSONArray ingredients = (JSONArray)this.get_obj().get("ingredients");
        JSONArray tags = (JSONArray)this.get_obj().get("tags");

        this.ingredients = toList(ingredients);
        this.tags = toList(tags);
    }


    private static <K> ArrayList<K> toList(JSONArray array) {
        ArrayList<K> out = new ArrayList<>();

        for (Object o : array) {
            out.add((K) o);
        }

        return out;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getContributor_id() {
        return contributor_id;
    }

    public Date getSubmitted() {
        return submitted;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String toString() {
        return this.name;
    }
}
