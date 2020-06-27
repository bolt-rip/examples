const k8s = require('@kubernetes/client-node');

const kc = new k8s.KubeConfig();
kc.loadFromFile("/home/emilien/.kube/config.bolt-us-central-1"); // change the location to your kubeconfig file

const k8sApi = kc.makeApiClient(k8s.CustomObjectsApi);

(async () => {
    try {
        const scaleStatusBody = (await k8sApi.getNamespacedCustomObjectScale("apps.kruise.io", "v1alpha1", "minecraft", "clonesets", "ranked")).body;
        const numberOfCurrentReplicas = scaleStatusBody.status.replicas;
        console.log(numberOfCurrentReplicas);

        const headers = { 'content-type': 'application/merge-patch+json' };
        const bodyPatch = { "spec": { "replicas": numberOfCurrentReplicas + 1 } };
        const scaleUpStatusBody = (await k8sApi.patchNamespacedCustomObjectScale("apps.kruise.io", "v1alpha1", "minecraft", "clonesets", "ranked", bodyPatch,
        undefined, undefined, undefined, { headers })).body;
        console.log(scaleUpStatusBody);

    } catch (error) {
        console.log(error.body);
    }
})();