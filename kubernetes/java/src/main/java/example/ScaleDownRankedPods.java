package example;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.json.JSONObject;

public class ScaleDownRankedPods {
    public static void main(String[] args) {
        try {
            // Initialize the Kubernetes client library
            KubernetesClient client = new DefaultKubernetesClient();

            // Custom Resource Definition is a way to interact with a custom block of the
            // API of k8s provided by an external actor
            CustomResourceDefinitionContext privateSrvCrdContext = new CustomResourceDefinitionContext.Builder()
                    .withName("clonesets.apps.kruise.io").withGroup("apps.kruise.io").withScope("Namespaced")
                    .withVersion("v1alpha1").withPlural("clonesets").build();

            // Get the current state of ranked cloneset into a JSON Object
            JSONObject rankedCDRJSONObject = new JSONObject(
                    client.customResource(privateSrvCrdContext).get("minecraft", "ranked"));

            // Get the number of replicas of the ranked cloneset
            Integer numberOfReplicas = rankedCDRJSONObject.getJSONObject("spec").getInt("replicas");

            // Substract the amount of replicas of the JSON Object of ranked
            rankedCDRJSONObject.getJSONObject("spec").put("replicas", numberOfReplicas - 1);

            // Send the JSON Object to the k8s API
            client.customResource(privateSrvCrdContext).edit("minecraft", "ranked", rankedCDRJSONObject.toString());

            // Close the kubernetes client (we can't reuse it after that) - Optional because
            // the library close it itself
            client.close();
        } catch (KubernetesClientException exception) {
            System.out.println(exception);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
