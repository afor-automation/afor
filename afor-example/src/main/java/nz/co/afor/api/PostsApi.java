package nz.co.afor.api;

import nz.co.afor.framework.api.rest.Get;
import nz.co.afor.framework.api.rest.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by Matt on 12/04/2016.
 */
@Component
public class PostsApi {
    @Autowired
    Get get;

    @Autowired
    Post post;

    @Value("${api.posts.uri}")
    String apiPostsUri;

    // Get multiple posts
    public ResponseEntity<nz.co.afor.model.Post[]> getPosts() {
        get.getHeaders().set(HttpHeaders.ACCEPT, String.valueOf(MediaType.APPLICATION_JSON));
        return get.request(apiPostsUri, nz.co.afor.model.Post[].class);
    }

    // Get a single post
    public ResponseEntity<nz.co.afor.model.Post> getPost(Integer id) {
        get.getHeaders().set(HttpHeaders.ACCEPT, String.valueOf(MediaType.APPLICATION_JSON));
        return get.request(String.format("%s/%s", apiPostsUri, id), nz.co.afor.model.Post.class);
    }

    // Create a post
    public ResponseEntity<nz.co.afor.model.Post> createPost(nz.co.afor.model.Post postToCreate) {
        post.getHeaders().set(HttpHeaders.ACCEPT, String.valueOf(MediaType.APPLICATION_JSON));
        return post.request(apiPostsUri, postToCreate, nz.co.afor.model.Post.class);
    }
}
