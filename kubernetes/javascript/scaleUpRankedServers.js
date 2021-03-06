const k8s = require('@kubernetes/client-node');

const kc = new k8s.KubeConfig();
kc.loadFromFile("/home/emilien/.kube/config"); // change the location to your kubeconfig file

const k8sApi = kc.makeApiClient(k8s.CustomObjectsApi);

(async () => {
    try {
        // fetch the number of current replicas
        const scaleStatusBody = (await k8sApi.getNamespacedCustomObjectScale("apps.kruise.io", "v1alpha1", "minecraft", "clonesets", "ranked")).body;
        const numberOfCurrentReplicas = scaleStatusBody.status.replicas;
        console.log(numberOfCurrentReplicas);

        //this tell to k8s to merge the new changes with the current changes
        const headers = { 'content-type': 'application/merge-patch+json' };
        //basically add one number to the current amount of replicas
        const bodyPatch = { "spec": { "replicas": numberOfCurrentReplicas + 1 } };

        // send a scale patch to the kruise controller
        const scaleUpStatusBody = (await k8sApi.patchNamespacedCustomObjectScale("apps.kruise.io", "v1alpha1", "minecraft", "clonesets", "ranked", bodyPatch,
        undefined, undefined, undefined, { headers })).body;
        console.log(scaleUpStatusBody);

    } catch (error) {
        console.log(error.body);
    }
})();
